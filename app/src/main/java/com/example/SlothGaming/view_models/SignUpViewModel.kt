package com.example.SlothGaming.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.SlothGaming.data.models.User
import com.example.SlothGaming.data.repository.AuthRepository
import com.example.SlothGaming.utils.InputValidator
import com.example.SlothGaming.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val authRepo: AuthRepository) : ViewModel() {
    private val _signupState = MutableStateFlow<Resource<User>?>(null)
    val signupState = _signupState.asStateFlow()

    fun signUpUser(
        firstName: String, lastName: String, email: String, phoneNumber: String, password: String,
        emptyFieldsError: String, emailFormatError: String, passwordShortError: String
    ) {
        InputValidator.validateSignUp(
            firstName, lastName, email, phoneNumber, password,
            emptyFieldsError, emailFormatError, passwordShortError
        )?.let {
            _signupState.value = Resource.error(it)
            return
        }

        viewModelScope.launch {
            _signupState.value = Resource.loading()
            val newUser = authRepo.createUser(firstName, lastName, email, phoneNumber, password)
            _signupState.value = newUser
        }
    }
}