package com.example.SlothGaming.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.SlothGaming.data.models.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Update
    fun updateUser(user: User)

    @Query("SELECT * FROM users")
    fun getUsers(): LiveData<List<User>>
}