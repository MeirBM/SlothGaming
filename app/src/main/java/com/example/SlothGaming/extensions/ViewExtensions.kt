package com.example.SlothGaming.extensions

import android.view.View

fun View.setScaleClickAnimation(
    scaleDown: Float = 0.96f,
    durationMs: Long = 80L,
    onClick: (() -> Unit)? = null
) {
    setOnClickListener { v ->
        v.animate()
            .scaleX(scaleDown)
            .scaleY(scaleDown)
            .setDuration(durationMs)
            .withEndAction {
                v.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(durationMs)
                    .withEndAction { onClick?.invoke() }
                    .start()
            }
            .start()
    }
}