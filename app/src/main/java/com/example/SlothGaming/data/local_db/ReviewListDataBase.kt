package com.example.SlothGaming.data.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.SlothGaming.data.models.GameItem
import com.example.SlothGaming.data.models.Review

@Database(entities = [Review::class,GameItem::class], version = 1, exportSchema = false)
abstract class ReviewListDataBase : RoomDatabase() {

    abstract fun reviewDao() : ReviewListDao
    abstract fun gameDao() : GameDao

    companion object {

        @Volatile
        private var instance:ReviewListDataBase? = null

        fun getDatabase(context: Context) = instance ?: synchronized(this) {
            Room.databaseBuilder(context.applicationContext, ReviewListDataBase::class.java,"reviews_db")
                .fallbackToDestructiveMigration(true).build()
            // TODO  switch fallbackToDestructiveMigration with AddMigration in Future
        }
    }
}