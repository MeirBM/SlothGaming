package com.example.SlothGaming.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishes")
data class Review(
    val title : String,
    val gameReview : String,
    val rating : Double,
    val photo : String?,

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

){


}