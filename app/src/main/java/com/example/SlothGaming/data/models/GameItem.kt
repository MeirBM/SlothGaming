package com.example.SlothGaming.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "games")
data class GameItem(
    @PrimaryKey (autoGenerate = true) val id: Int,
    val title: String,
    val platform:String?,
    val genre: String?=null,
    val imageUrl: String,
    val timeToBeat: Int? = null,
    val summary:String?,
    val section : String
) : Parcelable