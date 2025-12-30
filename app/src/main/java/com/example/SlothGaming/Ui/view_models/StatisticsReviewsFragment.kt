package com.example.SlothGaming.Ui.view_models

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import com.example.SlothGaming.R
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.SlothGaming.data.models.Review
import com.example.SlothGaming.data.repository.ReviewListRepository
import com.example.SlothGaming.databinding.StatisticsReviewsLayoutBinding

class StatisticsReviewsFragment : Fragment() {

    private val repository : ReviewListRepository by lazy{ ReviewListRepository(requireActivity().application) }
    private val viewModelFactory : ReviewViewModelFactory by lazy { ReviewViewModelFactory(repository) }
    private var _binding : StatisticsReviewsLayoutBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private val viewModel: ReviewViewModel by activityViewModels { viewModelFactory }

    private fun calculateStats(reviews: List<Review>) {

        //calculate avg for reviews // map for specify from rating
        val avg = reviews.map {
            it.rating
        }.average()

        //format for shorten the number after the .
        binding.avgRating.text = "%.1f".format(avg)
        binding.totalReviews.text = reviews.size.toString() //amount of reviews

        //group reviews by key & value: pc[review1,review2,...] then search for biggest if there isn't, then null.
        val topConsole = reviews.groupBy { it.console }
            .maxByOrNull { it.value.size }?.key ?: "N/A"

        binding.topConsole.text = topConsole
    }

    private fun emptyStats() {
        binding.avgRating.text = "0.0"
        binding.totalReviews.text = "0"
        binding.topConsole.text = "-"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.reviews?.observe(viewLifecycleOwner) { reviews ->
            if (reviews.isNotEmpty()) {
                calculateStats(reviews)
            } else {
                emptyStats()
            }
        }

        val menuHost : MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(
                menu: Menu,
                menuInflater: MenuInflater
            ) {
                menuInflater.inflate(R.menu.statistics_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true

            }

            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)
            }

            override fun onMenuClosed(menu: Menu) {
                super.onMenuClosed(menu)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}