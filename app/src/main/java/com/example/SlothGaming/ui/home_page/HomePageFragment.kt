package com.example.SlothGaming.ui.home_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.SlothGaming.R
import com.example.SlothGaming.ui.home_page.adapters.ParentAdapter
import com.example.SlothGaming.databinding.HomePageLayoutBinding
import com.example.SlothGaming.view_models.HomePageViewModel
import com.example.SlothGaming.utils.Error
import com.example.SlothGaming.utils.Loading
import com.example.SlothGaming.utils.Success
import com.example.SlothGaming.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class HomePageFragment: Fragment() {

    private var binding: HomePageLayoutBinding by autoCleared()

    lateinit var parentAdapter: ParentAdapter

    private val viewModel: HomePageViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomePageLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        val anim = AnimationUtils.loadAnimation(requireContext(),R.anim.spinning_for_sloth)
        binding.mainRecyclerView.apply {
            adapter = parentAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homePageState.collectLatest {
                    when (it.status) {
                        is Loading -> binding.loadingProgressHp.apply{
                        isVisible = true
                        startAnimation(anim)}
                        is Success -> {
                            binding.loadingProgressHp.apply{
                                clearAnimation()
                                isVisible = false
                            }
                            it.status.data?.let { sections ->
                                parentAdapter.submitList(sections)
                            }
                        }

                        is Error -> {
                            binding.loadingProgressHp.apply{
                                clearAnimation()
                                isVisible = false
                            Toast.makeText(
                                requireContext(),
                                it.status.message, Toast.LENGTH_LONG
                            ).show()

                            }
                        }

                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun setupAdapter() {
        // Initialize with an empty list initially
        parentAdapter = ParentAdapter{ selectedGame ->
                    val action = HomePageFragmentDirections
                        .actionHomePageFragmentToDetailReviewFragment(selectedGame)
                    findNavController().navigate(action)
                }
        }
    }


