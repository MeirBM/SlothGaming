package com.example.SlothGaming.Ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.SlothGaming.R
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.SlothGaming.Ui.view_models.ReviewViewModel
import com.example.SlothGaming.Ui.view_models.ReviewViewModelFactory
import com.example.SlothGaming.data.repository.ReviewListRepository
import com.example.SlothGaming.databinding.MyReviewsLayoutBinding
import com.example.SlothGaming.extensions.setScaleClickAnimation
import kotlin.getValue
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem

class MyReviewsFragment : Fragment() {

    private val repository : ReviewListRepository by lazy{ ReviewListRepository(requireActivity().application) }
    private val viewModelFactory : ReviewViewModelFactory by lazy { ReviewViewModelFactory(repository) }
    private var _binding : MyReviewsLayoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel : ReviewViewModel by activityViewModels {
        viewModelFactory
    }

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
            findNavController().navigate(R.id.action_myReviewsFragment_to_addReviewFragment)
        }

        //Populate the recycler view with reviews
        viewModel.reviews?.observe(viewLifecycleOwner) {
            binding.recycler.adapter = ReviewAdapter(it, object : ReviewAdapter.ReviewListener {

                override fun onReviewClicked(index: Int) {
                    Toast.makeText(requireContext(),
                        "${it[index]}",Toast.LENGTH_SHORT).show()
                }

                override fun onReviewLongClicked(index: Int) {
                    viewModel.setReview(it[index])
                    findNavController().navigate(R.id.action_myReviewsFragment_to_detailReviewFragment)

                }
            })
            binding.recycler.layoutManager = GridLayoutManager(requireContext(),1)
        }


        /* Define swipe to delete
           no drag action added currently
         */
        ItemTouchHelper(object : ItemTouchHelper.Callback() {

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) = makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

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
                builder.setTitle("DELETE")
                builder.setMessage("Are you sure you want to delete this review?")
                builder.setPositiveButton("Yes"){
                    dialog, which ->
                    val item = (binding.recycler.adapter as ReviewAdapter).reviewAt(viewHolder.absoluteAdapterPosition)
                    viewModel.deleteReview(item)
                }
                builder.setNegativeButton("No"){
                    dialog, which ->
                    binding.recycler.adapter?.notifyItemChanged(viewHolder.absoluteAdapterPosition)
                }
                builder.setCancelable(false)
                builder.show()


            }
        }).attachToRecyclerView(binding.recycler)


        /*Define menu
         menu include's:
           1) Statistics button which show's all my reviews stats
           2) Delete all reviews button which deletes all reviews
         */


        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            object : MenuProvider {

                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.review_page_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.stats_menu -> {
                            // Assuming you set the ID of the destination in your nav_graph to 'statisticsReviewsFragment'
                            // You might need to create an action in the nav graph connecting MyReviews to Statistics
                            findNavController().navigate(R.id.action_myReviewsFragment_to_statisticsReviewsFragment)
                            true
                        }

                        R.id.delete_all_reviews -> {
                            val dialog  = AlertDialog.Builder(requireContext()).apply {
                                setTitle("Delete All Reviews")
                                setMessage("Are you sure you want to delete all reviews?")
                                setPositiveButton("Yes"){
                                    dialog, which ->
                                    viewModel.deleteAll()
                                }
                                setNegativeButton("No"){
                                    _, _ ->
                                }
                                show()
                            }

                            true
                        }
                        else -> false
                    }
                }
            },
            viewLifecycleOwner
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
