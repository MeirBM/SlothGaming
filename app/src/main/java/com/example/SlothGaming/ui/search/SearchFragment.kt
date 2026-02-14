package com.example.SlothGaming.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.SlothGaming.databinding.SearchFragmentLayoutBinding
import com.example.SlothGaming.utils.Error
import com.example.SlothGaming.utils.Loading
import com.example.SlothGaming.utils.Success
import com.example.SlothGaming.utils.autoCleared
import com.example.SlothGaming.view_models.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var binding: SearchFragmentLayoutBinding by autoCleared()
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SearchFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchAdapter = SearchAdapter { selectedGame ->
            val action = SearchFragmentDirections
                .actionSearchFragmentToDetailReviewFragment(selectedGame)
            findNavController().navigate(action)
        }

        binding.searchRecyclerView.apply {
            adapter = searchAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        binding.searchEditText.addTextChangedListener { text ->
            viewModel.onQueryChanged(text?.toString().orEmpty())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchState.collectLatest { resource ->
                    when (resource.status) {
                        is Loading -> {
                            binding.searchProgressBar.isVisible = true
                            binding.noResultsText.isVisible = false
                        }
                        is Success -> {
                            binding.searchProgressBar.isVisible = false
                            val items = resource.status.data.orEmpty()
                            searchAdapter.submitList(items)
                            val hasQuery = binding.searchEditText.text?.isNotBlank() == true
                            binding.noResultsText.isVisible = items.isEmpty() && hasQuery
                        }
                        is Error -> {
                            binding.searchProgressBar.isVisible = false
                            binding.noResultsText.isVisible = false
                            Toast.makeText(
                                requireContext(),
                                resource.status.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }
}
