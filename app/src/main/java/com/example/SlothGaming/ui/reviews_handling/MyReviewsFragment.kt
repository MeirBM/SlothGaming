package com.example.SlothGaming.ui.reviews_handling

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.SlothGaming.R
import com.example.SlothGaming.view_models.ReviewViewModel
import com.example.SlothGaming.databinding.MyReviewsLayoutBinding
import com.example.SlothGaming.extensions.setScaleClickAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class MyReviewsFragment : Fragment() {
    private lateinit var reviewAdapter: ReviewAdapter

    private var _binding : MyReviewsLayoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel : ReviewViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MyReviewsLayoutBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addReviewButton.root.setScaleClickAnimation {
            viewModel.setReview(null)
            findNavController().navigate(R.id.action_myReviewsFragment_to_addReviewFragment)
        }

        reviewAdapter = ReviewAdapter( object : ReviewAdapter.ReviewListener {

            override fun onReviewClicked(index: Int) {
                val review = reviewAdapter.reviewAt(index)
                viewModel.setReview(review)
                Log.d("myreview","Here you get")
                findNavController().navigate(R.id.action_myReviewsFragment_to_detailReviewFragment)
            }

            override fun onReviewLongClicked(index: Int) {
                val review = reviewAdapter.reviewAt(index)
                viewModel.setReview(review)
                findNavController().navigate(R.id.action_myReviewsFragment_to_addReviewFragment)

            }
        })
        binding.recycler.adapter = reviewAdapter
        binding.recycler.layoutManager = GridLayoutManager(requireContext(), 1)


        //Populate the recycler view with reviews
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.reviews.collect {
                    reviewAdapter.submitList(it)
                }
            }
        }


            ItemTouchHelper(object : ItemTouchHelper.Callback() {

                override fun getMovementFlags(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ) = makeFlag(
                    ItemTouchHelper.ACTION_STATE_SWIPE,
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                )

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    TODO("Not yet implemented")
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    // Pop up to verify with user his action's
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle(getString(R.string.delete))
                    builder.setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_review))
                    builder.setPositiveButton(getString(R.string.yes)) { _,_ ->
                        val item =
                            (binding.recycler.adapter as ReviewAdapter).reviewAt(viewHolder.absoluteAdapterPosition)
                        viewModel.deleteReview(item)
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.review_has_been_deleted),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    builder.setNegativeButton(getString(R.string.no)) { _,_ ->
                        binding.recycler.adapter?.notifyItemChanged(viewHolder.absoluteAdapterPosition)
                    }
                    builder.setCancelable(false)
                    builder.show()


                }
            }).attachToRecyclerView(binding.recycler)

            val menuHost: MenuHost = requireActivity()
            menuHost.addMenuProvider(
                object : MenuProvider {

                    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                        menuInflater.inflate(R.menu.review_page_menu, menu)
                    }

                    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                        return when (menuItem.itemId) {
                            R.id.stats_menu -> {
                                if (viewModel.reviews.value.isEmpty()) {
                                    AlertDialog.Builder(requireContext())
                                        .setMessage(getString(R.string.there_are_no_statistics_to_show))
                                        .setPositiveButton(getString(R.string.ok), null)
                                        .show()
                                } else {
                                    findNavController().navigate(
                                        R.id.action_myReviewsFragment_to_statisticsReviewsFragment
                                    )
                                }
                                true
                            }

                            R.id.delete_all_reviews -> {
                                if (viewModel.reviews.value.isEmpty()) {
                                    AlertDialog.Builder(requireContext())
                                        .setMessage(getString(R.string.no_reviews_to_delete_menu))
                                        .setPositiveButton(getString(R.string.ok), null)
                                        .show()
                                } else {
                                    AlertDialog.Builder(requireContext()).apply {
                                        setTitle(getString(R.string.delete_all_reviews))
                                        setMessage(getString(R.string.are_you_sure_you_want_to_delete_all_reviews))
                                        setPositiveButton(getString(R.string.yes)) { _, _ ->

                                            viewModel.deleteAll()
                                            Toast.makeText(
                                                requireContext(),
                                                getString(R.string.all_reviews_has_been_deleted),
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                        setNegativeButton(getString(R.string.no)) { _, _ ->
                                        }
                                        show()
                                    }
                                }
                                true
                            }

                            else -> false
                        }
                    }
                },
                viewLifecycleOwner,
                Lifecycle.State.RESUMED
            )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}