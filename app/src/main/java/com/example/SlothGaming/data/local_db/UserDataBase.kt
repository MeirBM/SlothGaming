package com.example.SlothGaming.data.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.SlothGaming.data.models.User

@Database([User::class], version = 1, exportSchema = false)
abstract class UserDataBase : RoomDatabase(){

    abstract fun userDao() : UserDao

    companion object{
        @Volatile
        private var instance : UserDataBase? = null

        fun getDataBase(context: Context) = instance?:synchronized(this){

        Room.databaseBuilder(context.applicationContext, UserDataBase::class.java
            ,"UsersDB").allowMainThreadQueries()
            .build()
        }
    }


}