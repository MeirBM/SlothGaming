package com.example.SlothGaming.Ui.reviews_handling

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RatingBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.SlothGaming.R
import com.example.SlothGaming.Ui.view_models.ReviewViewModel
import com.example.SlothGaming.Ui.view_models.ReviewViewModelFactory
import com.example.SlothGaming.data.models.Review
import com.example.SlothGaming.data.repository.ReviewListRepository
import com.example.SlothGaming.databinding.AddReviewLayoutBinding
import com.example.SlothGaming.extensions.setScaleClickAnimation
import com.example.SlothGaming.utils.ColorProvider
import kotlin.math.roundToInt

class AddReviewFragment : Fragment() {
    companion object {
         private val consoleList = listOf<String>("PC","PS5","XBOX","Switch")
    }

    private val repository: ReviewListRepository by lazy { ReviewListRepository(requireActivity().application) }
    private val viewModelFactory: ReviewViewModelFactory by lazy { ReviewViewModelFactory(repository) }

    private val viewModel: ReviewViewModel by activityViewModels { viewModelFactory }
    private var _binding: AddReviewLayoutBinding? = null

    var imageUri: Uri? = null
    private val binding get() = _binding!!

    // Blocks ENTER only when the current line is empty or whitespace
    private val blockEnterOnEmptyLineFilter = InputFilter { source, start, end, dest, dstart, _ ->
        for (i in start until end) {
            if (source[i] == '\n') {
                val textBeforeCursor = dest.subSequence(0, dstart).toString()
                val currentLine = textBeforeCursor.substringAfterLast('\n')
                if (currentLine.trim().isEmpty()) {
                    return@InputFilter ""
                }
            }
        }
        null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddReviewLayoutBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reviewCheck = viewModel.chosenReview.value

        //Check if add a new review or edit chosen one
        if(reviewCheck != null){
            binding.enteredGameTitle.setText(reviewCheck.title)
            binding.enteredReview.setText(reviewCheck.gameReview)
            binding.consoleDropdown.setText(reviewCheck.console, false)
            binding.ratingBar.rating = reviewCheck.rating
            imageUri = reviewCheck.photo?.toUri()
            Glide.with(requireContext()).load(imageUri).into(binding.gameImage)

            binding.addReviewTitle.text = getString(R.string.edit_review)
            binding.addReviewButton.btnText.text = getString(R.string.edit_review_btn)

            val color = ColorProvider.pickColor(reviewCheck.rating, requireContext())
            binding.ratingBar.progressTintList = ColorStateList.valueOf(color)
        } else {
            binding.addReviewTitle.text = getString(R.string.add_a_review)
            binding.addReviewButton.btnText.text = getString(R.string.add_a_review)
        }

        changeColorOnRatingChange(binding.ratingBar)

        val adapter = ArrayAdapter(
            requireContext(), R.layout.console_list_layout,
            R.id.console_item, consoleList
        )

        binding.consoleDropdown.setAdapter(adapter)
        binding.consoleDropdown.setOnItemClickListener { _, _, _, _ ->
            binding.consoleLayout.error = null
        }

        // permission for photo library
        val pickImageLauncher: ActivityResultLauncher<Array<String>> =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) {
                if (it != null) {
                    binding.gameImage.setImageURI(it)
                    requireActivity().contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    imageUri = it
                }
            }

        // choose image from library
        binding.chooseImg.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        // Keep existing maxLength from XML, add Enter handling to BOTH fields
        binding.enteredReview.filters = binding.enteredReview.filters + blockEnterOnEmptyLineFilter
        binding.enteredGameTitle.filters = binding.enteredGameTitle.filters + blockEnterOnEmptyLineFilter

        // Add review text and statements
        binding.addReviewButton.root.setScaleClickAnimation {

            val minTitleLength = 3
            // trim for no whitespaces
            val title = binding.enteredGameTitle.text.toString().trim()
            val desc = binding.enteredReview.text.toString().trim()
            val ratingBar = binding.ratingBar.rating
            val consoleType = "${binding.consoleDropdown.text}".trim()

            when {
                consoleType.isEmpty() -> {
                    binding.consoleLayout.error = getString(R.string.please_enter_platform_type)
                    binding.consoleLayout.requestFocus()
                    return@setScaleClickAnimation
                }
                title.length < minTitleLength -> {
                    binding.enteredGameTitle.error = getString(R.string.please_enter_game_title_at_least_3_characters)
                    binding.enteredGameTitle.requestFocus()
                    return@setScaleClickAnimation
                }
                desc.isEmpty() -> {
                    binding.enteredReview.error = getString(R.string.please_enter_description)
                    binding.enteredReview.requestFocus()
                    return@setScaleClickAnimation
                }
                imageUri == null -> {
                    Toast.makeText(requireContext(), getString(R.string.please_upload_an_image), Toast.LENGTH_SHORT).show()
                    return@setScaleClickAnimation
                }
            }

            val image = imageUri.toString()



            val review = Review(title, desc, ratingBar, consoleType, image).apply {
                id = viewModel.chosenReview.value?.id ?: 0
            }

            if(review.id == 0){
                viewModel.addReview(review)
            }
            else{
                viewModel.updateReview(review)
            }
            viewModel.setReview(null)



            findNavController().navigate(
                R.id.action_addReviewFragment_to_myReviewsFragment, bundleOf("reviews" to review)
            )
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun changeColorOnRatingChange(ratingBar: RatingBar) {
        ratingBar.setOnTouchListener { v, event ->
            val context = requireContext()

            // Constrain it between 0 and 1 so it doesn't break if they slide off the edge
            val touchX = event.x.coerceIn(0f, v.width.toFloat())
            val fraction = touchX / v.width
            val rawRating = fraction * 5.0f

            // make it so rating value always jump in 0.5
            val steppedRating = (rawRating * 2).roundToInt() / 2.0f


            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {

                    // Update Color LIVE while sliding

                val color = ColorProvider.pickColor(steppedRating,context)

                    //change the xml
                    ratingBar.progressTintList = ColorStateList.valueOf(color)
                    ratingBar.secondaryProgressTintList = ColorStateList.valueOf(color)
                    //tell the RatingBar to update its stars to follow the finger
                    ratingBar.rating = steppedRating
                }
            }
            false
        }
    }
}