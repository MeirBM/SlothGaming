package com.example.SlothGaming.data.repository

import com.example.SlothGaming.data.local_db.GameDao
import com.example.SlothGaming.data.models.GameItem
import com.example.SlothGaming.data.models.GameResponse
import com.example.SlothGaming.data.remote_db.GameRemoteDataSource
import com.example.SlothGaming.utils.performFetchingAndSaving
import javax.inject.Inject


class HomeRepository @Inject constructor(
    private val remoteDataSource: GameRemoteDataSource,
    private val dao: GameDao
) {

    // 1. Top Rated Games
    fun getTopRatedGames() = performFetchingAndSaving(
        localDbFetch = { dao.getItemsBySection("top_rated") },
        remoteDbFetch = { remoteDataSource.getTopRatedGames() },
        localDbSave = { games ->
            val items = games.map {
                it.toGameItem(section = "top_rated", platformName = "Top Rated")
            }
            dao.insertAll(items)
        }
    )

    // 2. Coming Soon (Unwrapping the Release Date)
    fun getComingSoonGames() = performFetchingAndSaving(
        localDbFetch = { dao.getItemsBySection("coming_soon") },
        remoteDbFetch = { remoteDataSource.getComingSoonGames() },
        localDbSave = { responses ->
            // 1. mapNotNull filters out any nulls we return from the block
            val items = responses.mapNotNull { response ->
                val game = response.game

                // 2. Only proceed if we actually have a game object
                if (game != null) {
                    game.toGameItem(
                        section = "coming_soon",
                        // Use the date if available, otherwise a fallback string
                        platformName = response.release_date ?: "Coming Soon"
                    )
                } else {
                    // 3. Return null for this item so it gets skipped
                    null
                }
            }
            dao.insertAll(items)
        }
    )

    // 3. Publisher Spotlight (Unwrapping the Company Link)
    fun getPublisherSpotlight() = performFetchingAndSaving(
        localDbFetch = { dao.getItemsBySection("ubisoft_spotlight") },
        remoteDbFetch = { remoteDataSource.getPubSpotlightGames() },
        localDbSave = { responses ->
            // FIX: Use mapNotNull to filter out any nulls produced by the block
            val items = responses.mapNotNull { response ->
                // Safely handle if 'game' is null
                response.game?.toGameItem(
                    section = "ubisoft_spotlight",
                    platformName = "Ubisoft"
                )
            }
            dao.insertAll(items)
        }
    )

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