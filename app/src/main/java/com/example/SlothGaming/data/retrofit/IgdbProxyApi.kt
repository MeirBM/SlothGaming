package com.example.SlothGaming.data.retrofit
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Path


interface IgdbProxyApi {
    @POST("proxy/{endpoint}")
    suspend fun getIgdbData(
        @Path("endpoint") endpoint: String,
        @Body query: String
    ): Response<String>
}