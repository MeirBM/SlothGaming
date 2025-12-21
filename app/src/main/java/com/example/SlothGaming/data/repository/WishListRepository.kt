package com.example.SlothGaming.data.repository

import android.app.Application
import com.example.SlothGaming.data.models.Wish
import com.example.architectureproject.data.local_db.WishListDao
import com.example.architectureproject.data.local_db.WishListDataBase

class WishListRepository(val application: Application) {

        private var wishListDao: WishListDao?

        init {
            val db  = WishListDataBase.getDatabase(application.applicationContext)
            wishListDao = db?.wishDao()
        }

        fun getItems() = wishListDao?.getWish()

        fun addItem(wish: Wish) {
            wishListDao?.addWish(wish)
        }

        fun deleteItem(wish: Wish) {
            wishListDao?.deleteWish(wish)
        }

        fun getItem(id:Int)  = wishListDao?.getWish(id)

        fun deleteAll(){
            wishListDao?.deleteAll()
        }


    }