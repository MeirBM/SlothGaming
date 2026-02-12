package com.example.SlothGaming.ui.view_models

import android.util.Patterns
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
class SignUpViewModel @Inject constructor(private val authRepo: AuthRepository) : ViewModel() {
    private val _signupState = MutableStateFlow<Resource<User>?>(null)
    val signupState = _signupState.asStateFlow()

    fun signUpUser(
        firstName: String, lastName: String, email: String, phoneNumber: String, password: String
    ) {
        val error = when {
            firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()
                    || phoneNumber.isEmpty() || password.isEmpty() -> "Please fill all fields"

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "email does not match format"
            password.length < 8 -> "Password too short"

            else -> null
        }
        error?.let {
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