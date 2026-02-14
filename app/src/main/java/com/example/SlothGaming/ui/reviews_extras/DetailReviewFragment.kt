package com.example.SlothGaming.ui.reviews_extras

import android.os.Bundle
import android.util.Log
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
import com.example.SlothGaming.view_models.ReviewViewModel
import com.example.SlothGaming.databinding.DetailReviewBinding
import com.example.SlothGaming.utils.ColorProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailReviewFragment : Fragment() {
    private val star by lazy{ ContextCompat.
    getDrawable(requireContext(), R.drawable.ic_star)?.mutate()}
    var _binding : DetailReviewBinding?  = null

    // pass arguments by navArgs
    private val args : DetailReviewFragmentArgs by navArgs()


    val viewModel : ReviewViewModel by activityViewModels()

    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailReviewBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.chosenReview.collect { review ->   review?.let {
                    binding.reviewTitle.text = it.title
                    binding.reviewDesc.text = it.gameReview
                    binding.reviewConsole.text = getString(R.string.platform, it.console)
                    binding.ratingScore.text = it.rating.toString()
                    Glide.with(requireContext()).load(it.photo)
                        .into(binding.reviewedGameImage)
                    // match star color with rating
                    val color = ColorProvider.pickColor(it.rating.toDouble(), requireContext())
                    star?.setTint(color)
                    binding.ratingStar.setImageDrawable(star)
                }}
            }
        }

        val game = args.GameDetail // for getting current GameItem
        if (game != null) {
            val ratingOutOfFive = (game.rating ?: 0.0) / 20.0 // for getting rating between 0-5

            Log.d("NAV_DEBUG", "Sending Game: ${game.title}, Rating: ${game.rating}")
            binding.reviewTitle.text = game.title // push title
            binding.reviewDesc.text = game.summary// push description
            binding.reviewConsole.text = game.platform// push source
            binding.ratingScore.text = String.format("%.1f", ratingOutOfFive) // cut to .x number

            //Load image with glide
            Glide.with(this).load(game.imageUrl).into(binding.reviewedGameImage)
            val color = ColorProvider.pickColor(ratingOutOfFive, requireContext())
            star?.setTint(color)
            binding.ratingStar.setImageDrawable(star)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setReview(null)
        _binding=null
    }
}