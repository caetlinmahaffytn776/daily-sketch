package com.dailysketch.app.core.ui

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.dailysketch.app.R
import com.dailysketch.app.core.config.GameConfig
import com.dailysketch.app.databinding.ViewStatCardBinding

class StatCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ViewStatCardBinding.inflate(LayoutInflater.from(context), this)

    private var accentColor: Int = ContextCompat.getColor(context, R.color.terracotta)

    private var countAnimator: ValueAnimator? = null

    init {
        if (attrs != null) {
            val typed = context.obtainStyledAttributes(attrs, R.styleable.StatCardView)
            accentColor = typed.getColor(R.styleable.StatCardView_statAccent, accentColor)
            binding.statValue.text = typed.getString(R.styleable.StatCardView_statValue).orEmpty()
            binding.statLabel.text = typed.getString(R.styleable.StatCardView_statLabel).orEmpty()
            typed.recycle()
        }
        applyAccent()
    }

    fun bind(value: String, label: String) {
        binding.statValue.text = value
        binding.statLabel.text = label
        ViewCompat.setStateDescription(binding.statValue, label)
        visibility = View.VISIBLE
    }

    fun bindOrHide(value: Int, label: String) {
        if (value <= 0) {
            hide()
            return
        }
        bind(value.toString(), label)
    }

    fun countUpOrHide(value: Int, label: String) {
        if (value <= 0) {
            hide()
            return
        }
        bind(value.toString(), label)
        countAnimator?.cancel()
        val animator = ValueAnimator.ofInt(0, value)
        animator.duration = GameConfig.COUNT_UP_MS
        animator.addUpdateListener { running ->
            try {
                binding.statValue.text = running.animatedValue.toString()
            } catch (e: Exception) {
                animator.cancel()
            }
        }
        animator.start()
        countAnimator = animator
    }

    fun hide() {
        visibility = View.GONE
    }

    fun isCardVisible(): Boolean = visibility == View.VISIBLE

    fun cancelAnimations() {
        countAnimator?.cancel()
        countAnimator = null
    }

    private fun applyAccent() {
        val tint = ColorStateList.valueOf(accentColor)
        binding.statAccent.backgroundTintList = tint
        binding.statValue.setTextColor(accentColor)
    }
}
