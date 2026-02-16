package com.example.SlothGaming.data.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.SlothGaming.data.models.GameItem
import com.example.SlothGaming.data.models.Review

@Database(entities = [Review::class,GameItem::class], version = 1, exportSchema = false)
abstract class SlothGamingDataBase : RoomDatabase() {

    abstract fun reviewDao() : ReviewListDao
    abstract fun gameDao() : GameDao

    companion object {

        @Volatile//atomic action
        private var instance:SlothGamingDataBase? = null


        // create Local data base "reviews_db"
        fun getDatabase(context: Context) = instance ?: synchronized(this) {
            Room.databaseBuilder(context.applicationContext, SlothGamingDataBase::class.java,"reviews_db")
                .fallbackToDestructiveMigration(true).build()
        }
    }
}