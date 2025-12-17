package com.example.SlothGaming.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("Users")
data class User(
    @ColumnInfo("User name")
    val user: String,
    @ColumnInfo("User password")
    val password: String ) {
    @PrimaryKey(autoGenerate = true)
        var userId = 0
}