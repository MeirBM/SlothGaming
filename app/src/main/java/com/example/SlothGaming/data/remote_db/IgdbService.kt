package com.example.SlothGaming.data.remote_db
import com.example.SlothGaming.data.models.CompanyResponse
import com.example.SlothGaming.data.models.GameResponse
import com.example.SlothGaming.data.models.LatestResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.Path


interface IgdbService {

    @Headers("Content-Type: text/plain")
    @POST("proxy/games")
    suspend fun getLatestGames(@Body query: RequestBody): Response<List<GameResponse>>

    @Headers("Content-Type: text/plain")
    @POST("proxy/release_dates")
        suspend fun getPopularGames(@Body query: RequestBody): Response<List<LatestResponse>>

    @Headers("Content-Type: text/plain")
    @POST("proxy/involved_companies")
    suspend fun getTopRatedGames(@Body query: RequestBody): Response<List<CompanyResponse>>

}
