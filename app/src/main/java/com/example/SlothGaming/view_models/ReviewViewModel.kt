package com.example.SlothGaming.view_models


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.SlothGaming.data.models.Review
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
    ) : ViewModel(){
        //Converting the flow to stateFlow for saving calls (share a single resource)
    val reviews : StateFlow<List<Review>> = repository.getReviews()
        .stateIn(
        viewModelScope,
            //Keep the flow alive (5s) after UI disconnects to handle screen rotations
            SharingStarted.WhileSubscribed(5000),
            emptyList()
    )

    //Reactive state holder for Review UI
    private val _chosenReview = MutableStateFlow<Review?>(null)

    val chosenReview = _chosenReview.asStateFlow()




    fun setReview(review: Review?) {

        _chosenReview.value = review
    }
    // Updates the review in the database asynchronously without blocking the UI
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