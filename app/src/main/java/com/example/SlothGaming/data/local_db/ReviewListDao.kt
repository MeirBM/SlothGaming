package com.example.SlothGaming.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.SlothGaming.data.models.Review
@Dao
interface ReviewListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addReview(review: Review)

    @Delete
    suspend fun deleteReview(vararg review : Review)

    @Update
    suspend fun updateReview (review: Review)

    @Query("SELECT * FROM reviews ORDER BY rating ASC")
    fun getReviews(): LiveData<List<Review>>

    @Query("SELECT * FROM reviews WHERE id LIKE :id ")
    fun getReview(id : Int) : Review

    @Query("DELETE FROM reviews")
    suspend fun deleteAll()
}