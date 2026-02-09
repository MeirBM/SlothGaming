package com.example.SlothGaming.extensions

import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView


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
fun startLightingAnimation(imageView: ImageView) {
    val animator = ValueAnimator.ofInt(50, 150)
    animator.duration = 4000
    animator.repeatMode = ValueAnimator.REVERSE
    animator.repeatCount = ValueAnimator.INFINITE

    animator.addUpdateListener { animation ->
        val value = animation.animatedValue as Int
        // changing opacity over picture
        imageView.setColorFilter(Color.argb(value, 0, 0, 0), PorterDuff.Mode.SRC_ATOP)
    }

    animator.start()
}