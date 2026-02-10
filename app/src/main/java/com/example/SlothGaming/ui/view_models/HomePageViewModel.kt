package com.example.SlothGaming.ui.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.SlothGaming.data.models.User
import com.example.SlothGaming.data.repository.AuthRepository
import com.example.SlothGaming.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class HomePageViewModel @Inject constructor(private val authRepo: AuthRepository) : ViewModel() {
    private val _currentUser  = MutableStateFlow<Resource<User>?>(null)

    val currentUser = _currentUser.asStateFlow()

        init {
        viewModelScope.launch {
            _currentUser.value = Resource.Loading()
            _currentUser.value = authRepo.currentUser()
        }
    }

        fun isUserLoggedIn(): Boolean {
            return authRepo.isUserAuth()
    }
        fun useSignOut(){
        authRepo.logOut()
    }




    //TODO: HERE WE ADD ALL THE API FOR THE RECYCLERS?
}