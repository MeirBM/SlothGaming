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

    /*Reactive state holder(status:success,loading,etc) for search UI
    *Starts with an empty success list to prevent error/loading UI on first load
     */
    private val _searchState = MutableStateFlow<Resource<List<GameItem>>>(Resource.success(emptyList()))
    val searchState = _searchState.asStateFlow()
    //For preventing multiple requests to API/DB
    private var searchJob: Job? = null

    fun onQueryChanged(query: String) {
        // Cancel the previous search job if the user is still typing
        searchJob?.cancel()
        val trimmed = query.trim()
        // Prevent empty request & return emptyList
        if (trimmed.isBlank()) {
            _searchState.value = Resource.success(emptyList())
            return
        }
        searchJob = viewModelScope.launch {
            // Check if 300ms passed before making request
            delay(300)
            _searchState.value = Resource.loading()
            //Fetches the games from repository and updates the state
            _searchState.value = repository.searchGames(trimmed)
        }
    }
}
