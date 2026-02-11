package com.example.SlothGaming.data.models

data class GameItem(
    val id: Int,
    val title: String,
    val platform:List<String>?,
    val imageUrl: String,
    val summary:String?
    )