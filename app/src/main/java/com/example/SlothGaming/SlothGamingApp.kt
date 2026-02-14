package com.example.SlothGaming

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.SlothGaming.utils.NotificationHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SlothGamingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        NotificationHelper.createChannel(this)
    }
}