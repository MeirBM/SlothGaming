package com.example.SlothGaming.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.SlothGaming.data.models.User
import com.example.SlothGaming.data.repository.AuthRepository
import com.example.SlothGaming.utils.Error
import com.example.SlothGaming.utils.InputValidator
import com.example.SlothGaming.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepo: AuthRepository): ViewModel() {

    private val _loginStatus = MutableStateFlow<Resource<User>?>(null)

    val loginStatus = _loginStatus.asStateFlow()

    private val _currentUser  = MutableStateFlow<Resource<User>?>(null)

    val currentUser = _currentUser.asStateFlow()

    fun signInUser(email: String, password: String, emptyFieldsError: String, wrongCredentialsError: String) {
        InputValidator.validateLogin(email, password, emptyFieldsError)?.let {
            _loginStatus.value = Resource.error(it)
            return
        }
        viewModelScope.launch {
            _loginStatus.value = Resource.loading()
            val loginResult = authRepo.login(email, password)
            if (loginResult.status is Error) {
                _loginStatus.value = Resource.error(wrongCredentialsError)
            } else {
                _loginStatus.value = loginResult
            }
        }
    }

}

