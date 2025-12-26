package com.example.SlothGaming.Ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.SlothGaming.data.models.Review
import com.example.SlothGaming.data.repository.ReviewListRepository

class ReviewViewModel(private val repository : ReviewListRepository) : ViewModel(){
    val reviews : LiveData<List<Review>>? = repository.getReviews()

    private val _chosenReview = MutableLiveData<Review>()

    val chosenReview : LiveData<Review> get() = _chosenReview
    fun setReview(review:Review) {

        _chosenReview.value = review
    }
    fun addReview (review: Review) = repository.addReview(review)

    fun deleteReview (review: Review) = repository.deleteReview(review)

    fun deleteAll(){
        repository.deleteAll()
    }

}