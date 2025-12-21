package com.example.SlothGaming.Ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.SlothGaming.databinding.MyReviewsLayoutBinding

class MyReviewsFragment : Fragment() {

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

//        binding.addReviewButton.setOnClickListener {
//            // TODO: handle add review click
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
