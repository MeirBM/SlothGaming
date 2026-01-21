package com.example.SlothGaming.data.repository

import com.example.SlothGaming.data.models.User
import com.example.SlothGaming.utils.Resource

interface AuthRepository {
    suspend fun currentUser(): Resource<User>
    suspend fun createUser(firstName:String,lastName:String,email: String
                   ,phoneNumber:String,password: String): Resource<User>
    suspend fun login(email:String,password: String): Resource<User>
    fun logOut()
}