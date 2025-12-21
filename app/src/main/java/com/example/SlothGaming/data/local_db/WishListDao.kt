package com.example.architectureproject.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.SlothGaming.data.models.Wish
@Dao
interface WishListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItem(wish : Wish)

    @Delete
    fun deleteItem(wish : Wish)

    @Update
    fun updateItem (wish: Wish)

    @Query("SELECT * FROM wishes ORDER BY rating ASC")
    fun getItems(): LiveData<List<Wish>>

    @Query("SELECT * FROM wishes WHERE id LIKE :id ")
    fun getItem(id : Int) : Wish

    @Query("DELETE FROM wishes")
    fun deleteAll()
}