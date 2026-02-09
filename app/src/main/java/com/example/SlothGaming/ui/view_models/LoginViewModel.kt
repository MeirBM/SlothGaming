package com.example.SlothGaming.ui.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.SlothGaming.data.models.User
import com.example.SlothGaming.data.repository.AuthRepository
import com.example.SlothGaming.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val authRepo: AuthRepository): ViewModel() {

    private val _loginStatus = MutableStateFlow<Resource<User>?>(null)

    val loginStatus = _loginStatus.asStateFlow()

    private val _currentUser  = MutableStateFlow<Resource<User>?>(null)

    val currentUser = _currentUser.asStateFlow()


    init {
        viewModelScope.launch {
            _currentUser.value = Resource.Loading()
            _currentUser.value = authRepo.currentUser()
        }
    }

    fun signInUser(email: String,password:String){
        val error = if(email.isEmpty()||password.isEmpty()){
            "Please fill all the field's"
        }else null
        error?.let{
            _loginStatus.value = Resource.Error(error)
            return
        }
        viewModelScope.launch {
            _loginStatus.value = Resource.Loading()
            val loginResult =  authRepo.login(email, password)
            _loginStatus.value = loginResult
        }
    }

}

