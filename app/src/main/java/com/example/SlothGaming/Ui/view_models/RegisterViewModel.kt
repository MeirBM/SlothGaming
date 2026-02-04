package com.example.SlothGaming.Ui.view_models

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.SlothGaming.data.models.User
import com.example.SlothGaming.data.repository.AuthRepository
import com.example.SlothGaming.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class RegisterViewModel(private val authRepo: AuthRepository) : ViewModel() {
    private val _registerState = MutableStateFlow<Resource<User>?>(null)
    private val registerState = _registerState.asStateFlow()

    fun registerUser(
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
            _registerState.value = Resource.Error(it)
            return
        }
        _registerState.value = Resource.Loading()

        viewModelScope.launch {
            val newUser = authRepo.createUser(firstName, lastName, email, phoneNumber, password)
            _registerState.value = newUser
        }
    }


    class RegisterViewModelFactory(private val authRepo: AuthRepository) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RegisterViewModel(authRepo) as T

        }
    }
}