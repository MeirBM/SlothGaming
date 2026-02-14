package com.example.SlothGaming.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.SlothGaming.R
import com.example.SlothGaming.workers.GameReminderWorker
import java.util.concurrent.TimeUnit

object NotificationHelper {

    const val CHANNEL_ID = "game_reminder_channel"
    private const val WORK_NAME = "game_reminder_work"

    fun createChannel(context: Context) {
        val name = context.getString(R.string.notification_channel_name)
        val description = context.getString(R.string.notification_channel_description)
        val channel = NotificationChannel(
            CHANNEL_ID,
            name,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            this.description = description
        }
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    fun scheduleReminder(context: Context) {
        val request = PeriodicWorkRequestBuilder<GameReminderWorker>(
            24, TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}
