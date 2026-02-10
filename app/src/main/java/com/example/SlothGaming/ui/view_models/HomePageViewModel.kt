package com.example.SlothGaming.ui.view_models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.SlothGaming.data.models.GameItem
import com.example.SlothGaming.data.models.GameResponse
import com.example.SlothGaming.data.models.Section
import com.example.SlothGaming.data.models.User
import com.example.SlothGaming.data.repository.AuthRepository
import com.example.SlothGaming.data.retrofit.IgdbProxyApi
import com.example.SlothGaming.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class HomePageViewModel @Inject constructor(private val authRepo: AuthRepository,
    private val igdbApi: IgdbProxyApi) : ViewModel() {
    private val _currentUser  = MutableStateFlow<Resource<User>?>(null)

    val currentUser = _currentUser.asStateFlow()
    private val _homePageState = MutableStateFlow<Resource<List<Section>>>(Resource.Loading())
    val homePageState = _homePageState.asStateFlow()
        init {
        viewModelScope.launch {
//            _currentUser.value = Resource.Loading()
            _currentUser.value = authRepo.currentUser()
            fetchHomeContent()
        }
    }

        fun isUserLoggedIn(): Boolean {
            return authRepo.isUserAuth()
    }
        fun useSignOut(){
        authRepo.logOut()
    }

    //TODO: HERE WE ADD ALL THE API FOR THE RECYCLERS?
    fun fetchHomeContent() {
        // מפעילים על ה-Scope הדיפולטיבי (Main)
        viewModelScope.launch {
            _homePageState.value = Resource.Loading()
            try {
                // עוברים ל-IO רק עבור הקריאות עצמן
                val sections = withContext(Dispatchers.IO) {
                    val latestQuery = "fields name,cover.url; sort first_release_date desc; limit 10;"
                    val popularQuery = "fields name,cover.url; sort hypes desc; limit 10;"
                    val topRatedQuery = "fields name,cover.url; where total_rating_count > 10; sort total_rating desc; limit 10;"

                    val latestRes = async { igdbApi.getIgdbData("games", latestQuery) }.await()
                    val popularRes = async { igdbApi.getIgdbData("games", popularQuery) }.await()
                    val topRatedRes = async { igdbApi.getIgdbData("games", topRatedQuery) }.await()

//
                    Log.d("API_DEBUG", "${latestRes}")


                    val mutableSections = mutableListOf<Section>()
                    if (latestRes.isSuccessful) mutableSections.add(Section("Latest Releases", latestRes.body()?.mapToGameItem() ?: emptyList()))
                    if (popularRes.isSuccessful) mutableSections.add(Section("Most Popular", popularRes.body()?.mapToGameItem() ?: emptyList()))
                    if (topRatedRes.isSuccessful) mutableSections.add(Section("Top Rated", topRatedRes.body()?.mapToGameItem() ?: emptyList()))

                    mutableSections // מחזירים את הרשימה ל-Main Thread
                }

                _homePageState.value = Resource.Success(sections)

            } catch (e: Exception) {
                _homePageState.value = Resource.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }
    private fun List<GameResponse>.mapToGameItem(): List<GameItem> {
        return this.map { response ->
            GameItem(
                id = response.id.toInt(),
                title = response.name,
                imageUrl = response.cover?.url?.let { "https:$it" } ?: ""
            )
        }
    }
}