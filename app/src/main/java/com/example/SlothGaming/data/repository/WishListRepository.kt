package com.example.SlothGaming.data.repository

import android.app.Application
import com.example.SlothGaming.data.models.Wish
import com.example.architectureproject.data.local_db.WishListDao
import com.example.architectureproject.data.local_db.WishListDataBase

class WishListRepository(val application: Application) {

        private var wishListDao: WishListDao?

        init {
            val db  = WishListDataBase.getDatabase(application.applicationContext)
            wishListDao = db?.itemsDao()
        }

        fun getItems() = wishListDao?.getItems()

        fun addItem(wish: Wish) {
            wishListDao?.addItem(wish)
        }

        fun deleteItem(wish: Wish) {
            wishListDao?.deleteItem(wish)
        }

        fun getItem(id:Int)  = wishListDao?.getItem(id)

        fun deleteAll(){
            wishListDao?.deleteAll()
        }


    }