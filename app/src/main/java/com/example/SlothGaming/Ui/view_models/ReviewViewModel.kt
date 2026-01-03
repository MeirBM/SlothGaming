package com.example.SlothGaming.Ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.SlothGaming.data.models.Review
import com.example.SlothGaming.data.repository.ReviewListRepository
import kotlinx.coroutines.launch

class ReviewViewModel(private val repository : ReviewListRepository) : ViewModel(){
    val reviews : LiveData<List<Review>>? = repository.getReviews()

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