package com.example.SlothGaming.data.models

import com.google.gson.annotations.SerializedName

data class GameResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("cover") val cover: CoverResponse? = null
)

data class CoverResponse(
    @SerializedName("url") val url: String
)