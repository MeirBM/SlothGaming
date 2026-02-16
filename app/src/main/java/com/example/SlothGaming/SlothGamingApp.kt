package com.example.SlothGaming

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
/*Entry point for hilt DI
Manages the lifecycle of components (db,network, etc)
 */
@HiltAndroidApp
class SlothGamingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) // set default NightMode
    }
}