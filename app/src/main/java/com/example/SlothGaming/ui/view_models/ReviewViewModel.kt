package com.example.SlothGaming.ui.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.SlothGaming.data.fire_base.FirebaseAuthImpl
import com.example.SlothGaming.data.models.Review
import com.example.SlothGaming.data.repository.AuthRepository
import com.example.SlothGaming.data.repository.ReviewListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val repository : ReviewListRepository,
    private val authRepository: AuthRepository) : ViewModel(){
    //  TODO: Move from live data to Flow
    val reviews : StateFlow<List<Review>> = repository.getReviews()
        .stateIn(
        viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
    )

    private val _chosenReview = MutableStateFlow<Review?>(null)

    val chosenReview : StateFlow<Review?> get() = _chosenReview.asStateFlow()

    fun isUserLoggedIn(): Boolean {
        return authRepository.isUserAuth()
    }

    fun useSignOut(){
        authRepository.logOut()
    }
    fun setReview(review: Review?) {

        _chosenReview.value = review
    }

    fun updateReview(review: Review){
        viewModelScope.launch {
            repository.updateReview(review)
        }
    }
    fun addReview (review: Review) =
        viewModelScope.launch {
            repository.addReview(review)
        }

    fun deleteReview (review: Review) =
        viewModelScope.launch {
            repository.deleteReview(review)
        }

    fun deleteAll(){
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

}