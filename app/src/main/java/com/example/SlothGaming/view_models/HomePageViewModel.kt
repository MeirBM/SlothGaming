package com.example.SlothGaming.view_models

import android.net.http.UrlRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.SlothGaming.data.models.Section
import com.example.SlothGaming.data.repository.AuthRepository
import com.example.SlothGaming.data.repository.HomeRepository
import com.example.SlothGaming.utils.Resource
import com.example.SlothGaming.utils.Status
import com.example.SlothGaming.utils.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val repository: HomeRepository,
    private val authRepo: AuthRepository
) : ViewModel() {

    private val _homePageState = MutableStateFlow<Resource<List<Section>>>(Resource.loading())
    val homePageState = _homePageState.asStateFlow()

    init {
        fetchHomeContent()
    }

    fun isUserLoggedIn(): Boolean {
        return authRepo.isUserAuth()
    }
    fun useSignOut(){
        authRepo.logOut()
    }

    fun fetchHomeContent() {
        viewModelScope.launch {
            // Combine all 3 flows into one state
            combine(
                repository.getTopRatedGames(),
                repository.getComingSoonGames(),
                repository.getPublisherSpotlight()
            ) { topRated, comingSoon, ubisoft ->

                val sections = mutableListOf<Section>()

                if (topRated.status is Success) {
                    topRated.status.data?.let { games ->
                        sections.add(Section("Top Rated", games))
                    }
                }

                if (comingSoon.status is Success) {
                    comingSoon.status.data?.let { games ->
                        sections.add(Section("Coming Soon", games))
                    }
                }

                if (ubisoft.status is Success) {
                    ubisoft.status.data?.let { games ->
                        sections.add(Section("Ubisoft Spotlight", games))
                    }
                }

                if (sections.isNotEmpty()) {
                    Resource.success(sections)
                } else {
                    Resource.loading()
                }
            }.collectLatest { resource ->
                _homePageState.value = resource
            }
        }
    }
}