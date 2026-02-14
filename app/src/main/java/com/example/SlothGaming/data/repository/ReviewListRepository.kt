package com.example.SlothGaming.data.repository

import com.example.SlothGaming.data.models.Review
import com.example.SlothGaming.data.local_db.ReviewListDao
import javax.inject.Inject

class ReviewListRepository @Inject constructor(
    private val reviewListDao: ReviewListDao)
{
    fun getReviews() = reviewListDao.getReviews()

    suspend fun addReview(review: Review) {
        reviewListDao.addReview(review)
    }
    suspend fun updateReview (review: Review){
        reviewListDao.updateReview(review)
    }


    suspend fun deleteReview(review: Review) {
        reviewListDao.deleteReview(review)
        }

    suspend fun deleteAll(){
        reviewListDao.deleteAll()
    }
}