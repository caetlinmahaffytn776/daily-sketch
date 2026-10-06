package com.dailysketch.app.core.ui

import android.view.View
import android.view.animation.DecelerateInterpolator
import com.dailysketch.app.core.config.GameConfig

object ViewExtensions {

    fun staggerIn(views: List<View>, offsetPx: Float) {
        views.forEachIndexed { index, view ->
            view.alpha = 0f
            view.translationY = offsetPx
            view.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(GameConfig.ENTRANCE_ANIM_MS)
                .setStartDelay(index * GameConfig.ENTRANCE_STAGGER_MS)
                .setInterpolator(DecelerateInterpolator(1.5f))
                .start()
        }
    }

    fun popIn(view: View, durationMs: Long) {
        view.alpha = 0f
        view.scaleX = SCALE_FROM
        view.scaleY = SCALE_FROM
        view.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(durationMs)
            .setInterpolator(DecelerateInterpolator(1.2f))
            .start()
    }

    fun cancelAll(views: List<View>) {
        views.forEach { view ->
            view.animate().cancel()
            view.alpha = 1f
            view.translationY = 0f
            view.scaleX = 1f
            view.scaleY = 1f
        }
    }

    private const val SCALE_FROM = 0.84f
}
