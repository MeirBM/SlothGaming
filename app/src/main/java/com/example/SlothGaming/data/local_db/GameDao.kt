package com.example.SlothGaming.data.local_db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.SlothGaming.data.models.GameItem
import kotlinx.coroutines.flow.Flow


@Dao
interface GameDao {

    @Query("SELECT * FROM games WHERE section = :sectionName")
    fun getItemsBySection(sectionName: String): Flow<List<GameItem>>

    @Query("DELETE FROM games WHERE section = :sectionName")
    suspend fun clearSection(sectionName: String)


    @Transaction
    suspend fun updateSection(sectionName: String, games: List<GameItem>) {
        clearSection(sectionName)
        insertAll(games)
    }
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<GameItem>)

}