package com.example.SlothGaming.data.repository

import android.app.Application
import com.example.SlothGaming.data.models.Review
import com.example.architectureproject.data.local_db.ReviewListDao
import com.example.architectureproject.data.local_db.ReviewListDataBase

class ReviewListRepository(val application: Application) {

        private var reviewListDao: ReviewListDao?

        init {
            val db = ReviewListDataBase.getDatabase(application.applicationContext)
            reviewListDao = db.reviewDao()
        }

        fun getReviews() = reviewListDao?.getReview()

        fun addItem(review: Review) {
            reviewListDao?.addReview(review)
        }

        fun deleteReview(review: Review) {
            reviewListDao?.deleteReview(review)
        }

        fun getReview(id:Int)  = reviewListDao?.getReview(id)

        fun deleteAll(){
            reviewListDao?.deleteAll()
        }


    }