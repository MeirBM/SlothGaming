package com.example.architectureproject.data.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.SlothGaming.data.models.Review


@Database(entities = arrayOf(Review::class), version = 1, exportSchema = false)
abstract class ReviewListDataBase : RoomDatabase() {

    abstract fun reviewDao() : ReviewListDao

    companion object {

        @Volatile
        private var instance:ReviewListDataBase? = null

        fun getDatabase(context: Context) = instance ?: synchronized(this) {
            Room.databaseBuilder(context.applicationContext, ReviewListDataBase::class.java,"reviews_db")
                .fallbackToDestructiveMigration(true).allowMainThreadQueries().build()
        // TODO  switch fallbackToDestructiveMigration with AddMigration in Future
        }
    }
}