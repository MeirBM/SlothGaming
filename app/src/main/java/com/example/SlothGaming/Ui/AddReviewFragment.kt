package com.example.SlothGaming.Ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.core.content.ContextCompat
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
import com.example.SlothGaming.databinding.MyReviewsLayoutBinding

class AddReviewFragment : Fragment() {

    private val repository : ReviewListRepository by lazy{ ReviewListRepository(requireActivity().application) }
    private val viewModelFactory : ReviewViewModelFactory by lazy { ReviewViewModelFactory(repository) }

    private val viewModel : ReviewViewModel by activityViewModels { viewModelFactory }
    private var _binding : AddReviewLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddReviewLayoutBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ratingBar.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener{_,rating,_ ->
            val context = requireContext()

            val color = when{
                rating <=2.0f -> ContextCompat.getColor(context, R.color.lowRating)
                rating <=4.0f -> ContextCompat.getColor(context, R.color.midRating)
                else -> ContextCompat.getColor(context, R.color.highRating)
            }
            binding.ratingBar.progressTintList = ColorStateList.valueOf(color)


            binding.finishReviewBtn.setOnClickListener {

                val review = Review(binding.enteredGameTitle.text.toString(),binding.enteredReview.text.toString(),rating,null)

                viewModel.addReview(review)

                findNavController().navigate(
                    R.id.action_addReviewFragment_to_myReviewsFragment
                    , bundleOf("reviews" to review)
                )
            }


        }
//        val review =
//            Review(binding.enteredGameTitle.text.toString(),
//            binding.enteredReview.text.toString(),
//                )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}