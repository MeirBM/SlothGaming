package com.example.SlothGaming.data.remote_db
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject
class GameRemoteDataSource @Inject constructor (private val gameService: IgdbService)
    : BaseDataSource(){
    private fun String.toRawBody(): RequestBody {
        // This is the "Java style" way that always works
        val mediaType = MediaType.parse("text/plain")
        return RequestBody.create(mediaType, this)
    }
        suspend fun getTopRatedGames() = getResult {
            Log.d("API_DEBUG", "Calling Top Rated Games API...")
            val query = "fields name,platforms.name,cover.url,summary; sort total_rating desc; limit 10;"
            gameService.getLatestGames(query.toRawBody())}

        suspend fun getComingSoonGames() = getResult {
            val query = "fields game.name,game.platforms.name,game.cover.url,game.summary; where game.cover != null; sort date asc; limit 10;"
            gameService.getPopularGames(query.toRawBody()) }


        suspend fun getPubSpotlightGames() = getResult {
            val query = "fields game.name, game.platforms.name, game.cover.url, game.summary;where game.cover!=null; limit 10;"
            gameService.getTopRatedGames(query.toRawBody()) }
    }


