package com.example.SlothGaming.Ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.SlothGaming.R
import com.example.SlothGaming.Ui.view_models.ReviewViewModel
import com.example.SlothGaming.Ui.view_models.ReviewViewModelFactory
import com.example.SlothGaming.data.models.Review
import com.example.SlothGaming.data.repository.ReviewListRepository
import com.example.SlothGaming.databinding.AddReviewLayoutBinding
import com.example.SlothGaming.utils.ColorProvider
import kotlin.math.roundToInt

class AddReviewFragment : Fragment() {

    private val repository: ReviewListRepository by lazy { ReviewListRepository(requireActivity().application) }
    private val viewModelFactory: ReviewViewModelFactory by lazy { ReviewViewModelFactory(repository) }

    private val viewModel: ReviewViewModel by activityViewModels { viewModelFactory }
    private var _binding: AddReviewLayoutBinding? = null
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
        changeColorOnRatingChange(binding.ratingBar)

        binding.addReviewButton.root.setOnClickListener {
            val review = Review(
                binding.enteredGameTitle.text.toString(),
                binding.enteredReview.text.toString(),
                binding.ratingBar.rating,
                null)
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

    private fun changeColorOnRatingChange(ratingBar: RatingBar) {
        ratingBar.setOnTouchListener { v, event ->
            val context = requireContext()
            //color group
            val colors = ColorProvider.getColorMap(context.applicationContext)
            // Constrain it between 0 and 1 so it doesn't break if they slide off the edge
            val touchX = event.x.coerceIn(0f, v.width.toFloat())
            val fraction = touchX / v.width
            val rawRating = fraction * 5.0f
            // make it so rating value always jump in 0.5
            val steppedRating = (rawRating * 2).roundToInt() / 2.0f

            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    // Update Color LIVE while sliding

                    val color = when {
                        steppedRating <= 2.0f -> colors["Red"]!!
                        steppedRating <= 4.0f -> colors["Orange"]!!
                        else -> colors["Yellow"]!!
                    }
                    ratingBar.progressTintList = ColorStateList.valueOf(color)
                    ratingBar.secondaryProgressTintList = ColorStateList.valueOf(color)
                    //tell the RatingBar to update its stars to follow the finger
                    ratingBar.rating = steppedRating
                }
            }
            true
        }
    }
}