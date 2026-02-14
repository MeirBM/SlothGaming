package com.example.SlothGaming.ui.reviews_extras

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.SlothGaming.R
import com.example.SlothGaming.data.models.GameItem
import com.example.SlothGaming.data.models.Review
import com.example.SlothGaming.databinding.DetailReviewBinding
import com.example.SlothGaming.utils.ColorProvider
import com.example.SlothGaming.view_models.ReviewViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class DetailReviewFragment : Fragment() {

    private var _binding: DetailReviewBinding? = null
    private val binding get() = _binding!!

    private val star by lazy {
        ContextCompat.getDrawable(requireContext(), R.drawable.ic_star)?.mutate()
    }

    private val args: DetailReviewFragmentArgs by navArgs()
    private val viewModel: ReviewViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //If GameItem(GameDetail) Casting to Review
        args.GameDetail?.let { game ->
            viewModel.setReview(game.toReview())
        }

        // Listening to ViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.chosenReview.collect { review ->
                    review?.let { updateUI(it) }
                }
            }
        }
    }

    //Update UI
    private fun updateUI(review: Review) {
        binding.apply {
            reviewTitle.text = review.title
            reviewDesc.text = review.gameReview
            reviewConsole.text = getString(R.string.platform, review.console)
            val ratingValue = review.rating.toDouble()
            ratingScore.text = String.format("%.1f", ratingValue)

            Glide.with(requireContext())
                .load(review.photo)
                .into(reviewedGameImage)

            //Color change by rating
            val color = ColorProvider.pickColor(ratingValue, requireContext())
            star?.setTint(color)
            ratingStar.setImageDrawable(star)
        }
    }

    //To Review
    private fun GameItem.toReview(): Review {
        val ratingOutOfFive = (this.rating ?: 0.0) / 20.0
        return Review(
            title = this.title ?: "",
            gameReview = this.summary ?: getString(R.string.no_description_available),
            console = this.platform ?: getString(R.string.no_platform_available),
            rating = ratingOutOfFive.toFloat(),
            photo = this.imageUrl
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setReview(null)
        _binding = null
    }
}