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
    val platform:String? = null,
    val imageUrl: String,
    val rating: Double? = null,
    val summary:String? = null,
    val section : String
) : Parcelable