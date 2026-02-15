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
            val query = "fields name,rating,platforms.name,cover.url,summary;where name != null & rating_count > 1000; sort rating desc; limit 10;"
            gameService.getTopRatedGamesService(query.toRawBody())}

        suspend fun getComingSoonGames() = getResult {
            val query = "fields game.name,game.platforms.name,game.cover.url,game.summary,game.rating; where game.cover != null & date > 1767225600; sort date asc; limit 10;"
            gameService.getComingSoonGamesService(query.toRawBody()) }


        suspend fun getPubSpotlightGames() = getResult {
            val query = "fields game.name, game.platforms.name, game.cover.url, game.summary,game.rating;where company.name ~ *\"Ubisoft\"* & game.cover!=null; limit 10;"
            gameService.getPublisherSpotlightGamesService(query.toRawBody()) }

        suspend fun searchGames(searchQuery: String) = getResult {
            val query = "fields name,rating,platforms.name,cover.url,summary; where name ~ *\"$searchQuery\"* & cover.url!=null; limit 20;"
            gameService.searchGamesService(query.toRawBody()) }
    }


