package com.example.SlothGaming.Ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.SlothGaming.R
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.Callback.makeFlag
import androidx.recyclerview.widget.RecyclerView
import com.example.SlothGaming.Ui.view_models.ReviewViewModel
import com.example.SlothGaming.Ui.view_models.ReviewViewModelFactory
import com.example.SlothGaming.data.repository.ReviewListRepository
import com.example.SlothGaming.databinding.MyReviewsLayoutBinding
import kotlin.collections.get
import kotlin.getValue

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
        binding.addReviewButton.root.setOnClickListener {_ ->
            findNavController().navigate(R.id.action_myReviewsFragment_to_addReviewFragment)
        }

        arguments?.getString("review")?.let {

                Toast.makeText(requireActivity(),it,Toast.LENGTH_SHORT).show()
        }

        viewModel.reviews?.observe(viewLifecycleOwner) {
            Log.d("TEST", "reviews = ${viewModel.reviews}")
            binding.recycler.adapter = ReviewAdapter(it, object : ReviewAdapter.ReviewListener {

                override fun onReviewClicked(index: Int) {
                    Log.d("TEST", "reviews = ${viewModel.reviews}")

                    Toast.makeText(requireContext(),
                        "${it[index]}",Toast.LENGTH_SHORT).show()
                }

                override fun onReviewLongClicked(index: Int) {
                    viewModel.setReview(it[index])
                    findNavController().navigate(R.id.action_myReviewsFragment_to_detailReviewFragment2)
                }
            })
            binding.recycler.layoutManager = GridLayoutManager(requireContext(),1)
        }
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
                val item = (binding.recycler.adapter as ReviewAdapter).reviewAt(viewHolder.absoluteAdapterPosition)
                viewModel.deleteReview(item)

            }
        }).attachToRecyclerView(binding.recycler)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
