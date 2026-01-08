package com.example.SlothGaming.Ui.reviews_extras

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
import com.example.SlothGaming.R
import com.example.SlothGaming.Ui.view_models.ReviewViewModel
import com.example.SlothGaming.Ui.view_models.ReviewViewModelFactory
import com.example.SlothGaming.data.models.Review
import com.example.SlothGaming.data.repository.ReviewListRepository
import com.example.SlothGaming.databinding.StatisticsReviewsLayoutBinding
import com.example.SlothGaming.utils.ColorProvider
import kotlinx.coroutines.launch

class StatisticsReviewsFragment : Fragment() {

    private val star by lazy{ ContextCompat.
    getDrawable(requireContext(), R.drawable.ic_star)?.mutate()}
    private val repository : ReviewListRepository by lazy{ ReviewListRepository(requireActivity().application) }
    private val viewModelFactory : ReviewViewModelFactory by lazy {
        ReviewViewModelFactory(
            repository
        )
    }
    private var _binding : StatisticsReviewsLayoutBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = StatisticsReviewsLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val viewModel: ReviewViewModel by activityViewModels { viewModelFactory }

    private fun calculateStats(reviews: List<Review>) {

        //calculate avg for reviews // map for specify from rating
        val avg = reviews.map {
            it.rating
        }.average()
        val color = ColorProvider.pickColor(avg.toFloat(),requireContext())

        star?.setTint(color)
        binding.ratingStar.setImageDrawable(star)

        //format for shorten the number after the .
        binding.avgRating.text = "%.1f".format(avg)
        binding.totalReviews.text = reviews.size.toString() //amount of reviews

        //group reviews by key & value: pc[review1,review2,...] then search for biggest if there isn't, then null.
        val topConsole = reviews.groupBy { it.console }
            .maxByOrNull { it.value.size }?.key ?: getString(R.string.n_a)

        binding.topConsole.text = topConsole
    }

    private fun emptyStats() {
        star?.setTint(ContextCompat.getColor(requireContext(), R.color.lowRating))
        binding.ratingStar.setImageDrawable(star)
        binding.avgRating.text = "0.0"
        binding.totalReviews.text = "0"
        binding.topConsole.text = "-"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO: switch with lifeCycleOwner.lifeCycleScope.launch{....} to implement the flow.connect{...}
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.reviews.collect { reviews ->
                    if (reviews.isNotEmpty()) {
                        calculateStats(reviews)
                    } else {
                        emptyStats()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}