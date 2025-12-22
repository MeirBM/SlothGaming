package com.example.SlothGaming.Ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.SlothGaming.data.models.Review
import com.example.SlothGaming.data.repository.ReviewListRepository

class ReviewViewModel(private val repository : ReviewListRepository) : ViewModel(){
    val reviews : LiveData<List<Review>>? = repository.getReviews()

    fun addReview (review: Review) = repository.addReview(review)

    fun deleteReview (review: Review) = repository.deleteReview(review)

}