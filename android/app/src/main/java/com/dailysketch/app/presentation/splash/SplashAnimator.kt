package com.dailysketch.app.presentation.splash

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator

class SplashAnimator {

    private val animators = ArrayList<Animator>()

    private val animatedViews = ArrayList<View>()

    fun enterMark(mark: View) {
        mark.alpha = 0f
        mark.scaleX = MARK_SCALE_FROM
        mark.scaleY = MARK_SCALE_FROM
        mark.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(MARK_ENTER_MS)
            .setInterpolator(OvershootInterpolator(1.1f))
            .start()
        animatedViews.add(mark)
    }

    fun breatheMark(mark: View) {
        val scaleX = ObjectAnimator.ofFloat(mark, View.SCALE_X, 1f, MARK_BREATH_TO)
        val scaleY = ObjectAnimator.ofFloat(mark, View.SCALE_Y, 1f, MARK_BREATH_TO)
        for (animator in listOf(scaleX, scaleY)) {
            animator.duration = MARK_BREATH_MS
            animator.startDelay = MARK_ENTER_MS + MARK_BREATH_GAP_MS
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.repeatCount = ValueAnimator.INFINITE
            animator.repeatMode = ValueAnimator.REVERSE
            animator.start()
            animators.add(animator)
        }
    }

    fun enterTitle(title: View, subtitle: View, offsetPx: Float) {
        title.alpha = 0f
        title.translationY = offsetPx
        title.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(TITLE_ENTER_MS)
            .setStartDelay(TITLE_DELAY_MS)
            .setInterpolator(DecelerateInterpolator(1.6f))
            .start()
        animatedViews.add(title)

        subtitle.alpha = 0f
        subtitle.translationY = offsetPx * SUBTITLE_OFFSET_RATIO
        subtitle.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(TITLE_ENTER_MS)
            .setStartDelay(SUBTITLE_DELAY_MS)
            .setInterpolator(DecelerateInterpolator(1.4f))
            .start()
        animatedViews.add(subtitle)
    }

    fun driftDots(dots: List<View>, travelPx: Float) {
        dots.forEachIndexed { index, dot ->
            val animator = ObjectAnimator.ofFloat(
                dot,
                View.TRANSLATION_Y,
                -travelPx,
                travelPx
            )
            animator.duration = DOT_DURATIONS[index % DOT_DURATIONS.size]
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.repeatCount = ValueAnimator.INFINITE
            animator.repeatMode = ValueAnimator.REVERSE
            animator.start()
            animators.add(animator)
        }
    }

    fun pulseLabel(label: View) {
        val animator = ObjectAnimator.ofFloat(label, View.ALPHA, LABEL_ALPHA_LOW, 1f)
        animator.duration = LABEL_PULSE_MS
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.repeatCount = ValueAnimator.INFINITE
        animator.repeatMode = ValueAnimator.REVERSE
        animator.start()
        animators.add(animator)
    }

    fun fadeFooter(footer: View) {
        footer.alpha = 0f
        footer.animate()
            .alpha(FOOTER_ALPHA)
            .setDuration(FOOTER_FADE_MS)
            .setStartDelay(FOOTER_DELAY_MS)
            .setInterpolator(DecelerateInterpolator())
            .start()
        animatedViews.add(footer)
    }

    fun cancelAll() {
        animators.forEach { it.cancel() }
        animators.clear()
        animatedViews.forEach { it.animate().cancel() }
        animatedViews.clear()
    }

    private companion object {
        const val MARK_SCALE_FROM = 0.84f
        const val MARK_BREATH_TO = 1.03f
        const val MARK_ENTER_MS = 560L
        const val MARK_BREATH_GAP_MS = 120L
        const val MARK_BREATH_MS = 2600L
        const val TITLE_ENTER_MS = 520L
        const val TITLE_DELAY_MS = 180L
        const val SUBTITLE_DELAY_MS = 420L
        const val SUBTITLE_OFFSET_RATIO = 0.6f
        const val LABEL_PULSE_MS = 1400L
        const val LABEL_ALPHA_LOW = 0.4f
        const val FOOTER_FADE_MS = 620L
        const val FOOTER_DELAY_MS = 640L
        const val FOOTER_ALPHA = 0.85f
        val DOT_DURATIONS = longArrayOf(2400L, 3100L, 2700L)
    }
}
