package com.example.SlothGaming.data.models

import com.google.gson.annotations.SerializedName

// Serialize is used to make sure Response key matches the val's
data class GameResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("cover") val cover: CoverResponse? = null,
    @SerializedName("platforms") val platforms: List<PlatformResponse>? = null
    ,@SerializedName("summary") val summary :String?
    ,@SerializedName("rating") val rating: Double? = null

)
data class CompanyResponse(
    @SerializedName("game") val game: GameResponse?
)
data class LatestResponse(
    @SerializedName("game") val game: GameResponse?
)

data class CoverResponse(
    @SerializedName("url") val url: String
)
data class PlatformResponse(
    @SerializedName("name") val name: String
)

