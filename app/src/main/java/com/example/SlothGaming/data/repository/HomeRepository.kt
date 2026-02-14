package com.example.SlothGaming.data.repository

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.example.SlothGaming.data.local_db.GameDao
import com.example.SlothGaming.data.models.GameItem
import com.example.SlothGaming.data.models.GameResponse
import com.example.SlothGaming.data.remote_db.GameRemoteDataSource
import com.example.SlothGaming.utils.Resource
import com.example.SlothGaming.utils.Success
import com.example.SlothGaming.utils.performFetchingAndSaving
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class HomeRepository @Inject constructor(
    private val remoteDataSource: GameRemoteDataSource,
    private val dao: GameDao,
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val PREF_NAME = "SlothPref"
        private const val KEY_LAST_FETCH = "lastHomeFetch"
        private const val FETCH_INTERVAL_MS = 60 * 60 * 1000L // 1 hour
    }

    private fun shouldFetchFromRemote(): Boolean {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val lastFetch = pref.getLong(KEY_LAST_FETCH, 0)
        return System.currentTimeMillis() - lastFetch > FETCH_INTERVAL_MS
    }

    private fun updateLastFetchTime() {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit { putLong(KEY_LAST_FETCH, System.currentTimeMillis()) }
    }

    // 1. Top Rated Games
    fun getTopRatedGames() = performFetchingAndSaving(
        localDbFetch = { dao.getItemsBySection("top_rated") },
        remoteDbFetch = { remoteDataSource.getTopRatedGames() },
        localDbSave = { games ->
            val items = games.map {
                it.toGameItem(section = "top_rated", platformName = it.platforms?.first()?.name?:"")
            }
            dao.updateSection("top_rated",items)
            updateLastFetchTime()
        },
        shouldFetch = shouldFetchFromRemote()
    )

    // 2. Coming Soon (Unwrapping the Release Date)
    fun getComingSoonGames() = performFetchingAndSaving(
        localDbFetch = { dao.getItemsBySection("coming_soon") },
        remoteDbFetch = { remoteDataSource.getComingSoonGames() },
        localDbSave = { responses ->
            val items = responses.mapNotNull { response ->
                val game = response.game
                game?.toGameItem(
                    section = "coming_soon",
                    platformName = response.game.platforms?.first()?.name?:""
                )
            }
            Log.d("items","$items")
            dao.updateSection("coming_soon", items)
        },
        shouldFetch = shouldFetchFromRemote()
    )

    // 3. Publisher Spotlight (Unwrapping the Company Link)
    fun getPublisherSpotlight() = performFetchingAndSaving(
        localDbFetch = { dao.getItemsBySection("ubisoft_spotlight") },
        remoteDbFetch = { remoteDataSource.getPubSpotlightGames() },
        localDbSave = { responses ->
            val items = responses.mapNotNull { response ->
                response.game?.toGameItem(
                    section = "ubisoft_spotlight",
                    platformName = response.game.platforms?.first()?.name?:""
                )
            }
            dao.updateSection("ubisoft_spotlight",items)
        },
        shouldFetch = shouldFetchFromRemote()
    )

    // Search games — API only, no local caching
    suspend fun searchGames(query: String): Resource<List<GameItem>> {
        val result = remoteDataSource.searchGames(query)
        return if (result.status is Success) {
            val items = result.status.data!!.map {
                it.toGameItem(
                    section = "search",
                    platformName = it.platforms?.firstOrNull()?.name ?: ""
                )
            }
            Resource.success(items)
        } else {
            Resource.error((result.status as com.example.SlothGaming.utils.Error).message)
        }
    }

    // Helper to map GameResponse -> GameItem Entity
    private fun GameResponse.toGameItem(section: String, platformName: String): GameItem {
        val rawUrl = this.cover?.url?:""
        val highResUrl = rawUrl.replace("t_thumb", "t_720p").let {
            if (it.startsWith("//")) "https:$it" else it
        }
        return GameItem(
            id = this.id.toInt(),
            title = this.name,
            rating = this.rating,
            imageUrl = highResUrl,
            summary = this.summary,
            platform = platformName,
            section = section
        )
    }
}