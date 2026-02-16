package com.example.SlothGaming.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.SlothGaming.R
import com.example.SlothGaming.data.models.Section
import com.example.SlothGaming.data.repository.AuthRepository
import com.example.SlothGaming.data.repository.HomeRepository
import com.example.SlothGaming.utils.Resource
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

    //Reactive state holder for homepage UI(status:success,loading,etc)
    private val _homePageState = MutableStateFlow<Resource<List<Section>>>(Resource.loading())

    // Letting the fragment to observe ^
    val homePageState = _homePageState.asStateFlow()

    init {
        fetchHomeContent()
    }
    // Check if user is logged in from firebase localMemory(Token Timeout)
    fun isUserLoggedIn(): Boolean {
        return authRepo.isUserAuth()
    }
    fun useSignOut(){
        authRepo.logOut()
    }

    // For Sections rebuild
    private fun fetchHomeContent() {
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
                        //rebuild the sections
                        sections.add(Section(R.string.section_top_rated, games))
                    }
                }

                if (comingSoon.status is Success) {
                    comingSoon.status.data?.let { games ->
                        sections.add(Section(R.string.section_coming_soon, games))
                    }
                }

                if (ubisoft.status is Success) {
                    ubisoft.status.data?.let { games ->
                        sections.add(Section(R.string.section_ubisoft_spotlight, games))
                    }
                }
                // Present a section even if others in the combine are not loaded yet
                if (sections.isNotEmpty()) {
                    Resource.success(sections)
                } else {
                    Resource.loading()
                }
                // CollectLatest to ensure ignore old data & prevent race condition
            }.collectLatest { resource ->
                _homePageState.value = resource
            }
        }
    }
}