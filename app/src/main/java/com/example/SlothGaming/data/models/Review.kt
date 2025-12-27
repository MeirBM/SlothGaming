package com.example.SlothGaming.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "reviews")
data class Review(
    val title : String,
    val gameReview : String,
    val rating : Float,
    val console: String,
    val photo : String?) : Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}