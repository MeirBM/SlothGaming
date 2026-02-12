package com.example.SlothGaming.data.local_db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.SlothGaming.data.models.GameItem
import kotlinx.coroutines.flow.Flow


@Dao
interface GameDao {

    @Query("SELECT * FROM games WHERE section = :sectionName")
    fun getItemsBySection(sectionName: String): Flow<List<GameItem>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<GameItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGames(game: List<GameItem>)

    @Query("SELECT * FROM games WHERE id = :id")
    fun getGame(id:Int) : Flow<GameItem>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGames(game: List<GameItem>)

    @Query("SELECT * FROM games")
    fun getGames() : Flow<List<GameItem>>




}