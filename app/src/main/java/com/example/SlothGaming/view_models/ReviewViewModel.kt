package com.example.SlothGaming.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.SlothGaming.data.models.Review
import com.example.SlothGaming.data.repository.ReviewListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val repository : ReviewListRepository,
    ) : ViewModel(){
    val reviews : StateFlow<List<Review>> = repository.getReviews()
        .stateIn(
        viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
    )

    private val _chosenReview = MutableLiveData<Review?>()

    val chosenReview : LiveData<Review?> get() = _chosenReview




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