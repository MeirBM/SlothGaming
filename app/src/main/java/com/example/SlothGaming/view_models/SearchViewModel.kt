package com.example.SlothGaming.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.SlothGaming.data.models.GameItem
import com.example.SlothGaming.data.repository.HomeRepository
import com.example.SlothGaming.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel() {

    private val _searchState = MutableStateFlow<Resource<List<GameItem>>>(Resource.success(emptyList()))
    val searchState = _searchState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChanged(query: String) {
        searchJob?.cancel()
        val trimmed = query.trim()
        if (trimmed.isBlank()) {
            _searchState.value = Resource.success(emptyList())
            return
        }
        searchJob = viewModelScope.launch {
            delay(300)
            _searchState.value = Resource.loading()
            _searchState.value = repository.searchGames(trimmed)
        }
    }
}
