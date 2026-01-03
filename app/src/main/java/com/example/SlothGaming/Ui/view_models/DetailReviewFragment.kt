package com.example.SlothGaming.Ui.view_models

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.SlothGaming.R
import com.example.SlothGaming.databinding.DetailReviewBinding
import com.example.SlothGaming.utils.ColorProvider
import kotlin.getValue


class DetailReviewFragment : Fragment() {
    private val star by lazy{ ContextCompat.
    getDrawable(requireContext(),R.drawable.ic_star)?.mutate()}
    var _binding : DetailReviewBinding?  = null


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


        viewModel.chosenReview.observe(viewLifecycleOwner){
            // populate the view with data
            binding.reviewTitle.text=it.title
            binding.reviewDesc.text=it.gameReview
            binding.reviewConsole.text = it.console
            binding.ratingScore.text = it.rating.toString()
            Glide.with(requireContext()).load(it.photo)
                .into(binding.reviewedGameImage)
            // match star color with rating
            val color = ColorProvider.pickColor(it.rating,requireContext())

            star?.setTint(color)
            binding.ratingStar.setImageDrawable(star)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}