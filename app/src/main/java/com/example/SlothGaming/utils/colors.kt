package com.example.SlothGaming.utils

import android.content.Context

import androidx.core.content.ContextCompat
import com.example.SlothGaming.R


object ColorProvider {
    fun getColorMap(context: Context): Map<String, Int> {
        return mapOf(
            "Red" to ContextCompat.getColor(context, R.color.lowRating),
            "Orange" to ContextCompat.getColor(context, R.color.midRating),
            "Yellow" to ContextCompat.getColor(context, R.color.highRating)
        )

    }
    fun pickColor(rating: Double, context: Context): Int{

        val color = when {
            rating <= 2.0f -> ContextCompat.getColor(context, R.color.lowRating)
            rating <= 4.0f -> ContextCompat.getColor(context, R.color.midRating)
            else -> ContextCompat.getColor(context, R.color.highRating)
        }
        return color
    }
}