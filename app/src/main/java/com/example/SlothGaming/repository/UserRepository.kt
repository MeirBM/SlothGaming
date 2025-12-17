package com.example.SlothGaming.repository

import android.app.Application
import com.example.SlothGaming.data.local_db.UserDao
import com.example.SlothGaming.data.local_db.UserDataBase

class UserRepository(application: Application){

    private val userDao: UserDao?

    init {
        val db = UserDataBase.getDataBase(application.applicationContext)
        userDao = db.userDao()
    }
}