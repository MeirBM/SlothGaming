package com.example.SlothGaming.ui.home_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
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


        binding.mainRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homePageState.collectLatest {
                    when (it.status) {
                        is Loading -> binding.homeProgress.isVisible = true
                        is Success -> {
                            it.status.data?.let { section ->
                                binding.mainRecyclerView.adapter = ParentAdapter(section)
                            }
                            binding.homeProgress.isVisible = false

                        }

                        is Error -> Toast.makeText(
                            requireContext(),
                            it.status.message, Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}
