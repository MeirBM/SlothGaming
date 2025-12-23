package com.example.SlothGaming.Ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.SlothGaming.R
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.SlothGaming.Ui.view_models.ReviewViewModelFactory
import com.example.SlothGaming.data.repository.ReviewListRepository
import com.example.SlothGaming.databinding.MyReviewsLayoutBinding

class MyReviewsFragment : Fragment() {

    private val repository : ReviewListRepository by lazy{ ReviewListRepository(requireActivity().application) }
    private val viewModelFactory : ReviewViewModelFactory by lazy { ReviewViewModelFactory(repository) }
    private var _binding : MyReviewsLayoutBinding? = null
    private val binding get() = _binding!!

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
