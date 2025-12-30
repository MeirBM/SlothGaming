package com.example.SlothGaming.Ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RatingBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.core.view.ViewCompat
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
         private val consoleList = listOf<String>("Ps5", "Xbox", "PC","Switch")
    }

    private val repository: ReviewListRepository by lazy { ReviewListRepository(requireActivity().application) }
    private val viewModelFactory: ReviewViewModelFactory by lazy { ReviewViewModelFactory(repository) }

    private val viewModel: ReviewViewModel by activityViewModels { viewModelFactory }
    private var _binding: AddReviewLayoutBinding? = null

    var imageUri: Uri? = null
    private val binding get() = _binding!!

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

        // Force LTR for this screen, even when text is Hebrew
        ViewCompat.setLayoutDirection(binding.root, ViewCompat.LAYOUT_DIRECTION_LTR)

        binding.enteredGameTitle.textDirection = View.TEXT_DIRECTION_LTR
        binding.enteredGameTitle.textAlignment = View.TEXT_ALIGNMENT_VIEW_START

        binding.enteredReview.textDirection = View.TEXT_DIRECTION_LTR
        binding.enteredReview.textAlignment = View.TEXT_ALIGNMENT_VIEW_START

        binding.consoleDropdown.textDirection = View.TEXT_DIRECTION_LTR
        binding.consoleDropdown.textAlignment = View.TEXT_ALIGNMENT_VIEW_START

        changeColorOnRatingChange(binding.ratingBar)

        val adapter = ArrayAdapter(requireContext(),R.layout.console_list_layout,
            R.id.console_item,consoleList)

        binding.consoleDropdown.setAdapter(adapter)
        binding.consoleDropdown.setOnItemClickListener { _, _, _, _ ->
            binding.consoleDropdown.error = null
        }

        //permission for photo library
        val pickImageLauncher : ActivityResultLauncher<Array<String>> =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) {
                binding.gameImage.setImageURI(it)
                if (it != null) {
                    requireActivity().contentResolver.takePersistableUriPermission(it,Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                imageUri = it
            }


        binding.chooseImg.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        binding.addReviewButton.root.setScaleClickAnimation {

                val minTitleLength = 3

                val title = binding.enteredGameTitle.text.toString().trim()
                val desc = binding.enteredReview.text.toString().trim()
                val ratingBar = binding.ratingBar.rating
                val consoleType = "${binding.consoleDropdown.text}".trim()


            when{
                consoleType.isEmpty() ->{
                    binding.consoleDropdown.error = "Please Enter Platform Type"
                    binding.consoleDropdown.requestFocus()
                    return@setScaleClickAnimation}
                title.length < minTitleLength -> {
                    binding.consoleDropdown.error = "Please Enter Platform Type"
                    binding.consoleDropdown.requestFocus()
                    return@setScaleClickAnimation}
                desc.isEmpty()->{
                    binding.enteredReview.error = "Please Enter Description"
                    binding.enteredReview.requestFocus()
                    return@setScaleClickAnimation
                }
            }
            if (imageUri == null) {
                Toast.makeText(requireContext(), "Please upload an image", Toast.LENGTH_SHORT).show()
                return@setScaleClickAnimation
            }
            val image = imageUri.toString()


            val review = Review(title,desc,ratingBar,consoleType,image)
            viewModel.addReview(review)


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