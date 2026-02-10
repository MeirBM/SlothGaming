package com.example.SlothGaming.data.retrofit
import com.example.SlothGaming.data.models.GameResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.Path


interface IgdbProxyApi {
    @Headers("Content-Type: text/plain")
    @POST("proxy/{endpoint}")
    suspend fun getIgdbData(
        @Path("endpoint") endpoint: String,
        @Body query: String
    ): Response<List<GameResponse>>
}