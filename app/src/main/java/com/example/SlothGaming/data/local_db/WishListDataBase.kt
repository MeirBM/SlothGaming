package com.example.architectureproject.data.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.SlothGaming.data.models.Wish


@Database(entities = arrayOf(Wish::class), version = 1, exportSchema = false)
abstract class WishListDataBase : RoomDatabase() {

    abstract fun itemsDao() : WishListDao

    companion object {

        @Volatile
        private var instance:WishListDataBase? = null

        fun getDatabase(context: Context) = instance ?: synchronized(this) {
            Room.databaseBuilder(context.applicationContext, WishListDataBase::class.java,"items_db")
                .allowMainThreadQueries().build()
        }
    }
}