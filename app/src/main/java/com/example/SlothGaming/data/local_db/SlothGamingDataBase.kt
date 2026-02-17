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
        @Volatile/* Ensures that the value of 'instance' is always up-to-date and visible to all threads immediately
        Read&Write atomic action through
        */
        private var instance:SlothGamingDataBase? = null


        // create Local data base "slothgaming_db"
        fun getDatabase(context: Context): SlothGamingDataBase {
            //check if not null, return and don't synchronize
            return instance ?: synchronized(this) {
                // Check that another thread has not created the db
                instance?: Room.databaseBuilder(
                    context.applicationContext,
                    SlothGamingDataBase::class.java,
                    "slothgaming_db"
                )
                    .fallbackToDestructiveMigration().build().also {
                        instance = it //Saving the instance
                    }
            }
        }
    }
}