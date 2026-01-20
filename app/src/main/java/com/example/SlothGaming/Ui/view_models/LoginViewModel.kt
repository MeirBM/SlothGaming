package com.example.SlothGaming.Ui.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.SlothGaming.data.models.User
import com.example.SlothGaming.data.repository.AuthRepository
import com.example.SlothGaming.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepo: AuthRepository): ViewModel() {

    private val _loginStatus = MutableStateFlow<Resource<User>?>(null)

    private val loginStatus = _loginStatus.asStateFlow()

    private val _currentUser  = MutableStateFlow<Resource<User>?>(null)

    private val currentUser = _currentUser.asStateFlow()

    private val userRef = FirebaseFirestore.getInstance().collection("users")


    init {
        _currentUser.value = Resource.Loading()
        viewModelScope.launch {
            _currentUser.value = authRepo.currentUser()
        }
    }

    fun signInUser(email: String,password:String){


        }

    }





class LoginViewModelFactory(private val authRepo: AuthRepository): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(authRepo) as T
    }
}