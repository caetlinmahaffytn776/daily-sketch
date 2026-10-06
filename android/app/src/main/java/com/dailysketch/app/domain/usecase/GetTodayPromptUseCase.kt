package com.dailysketch.app.domain.usecase

import com.dailysketch.app.domain.model.PromptPack
import com.dailysketch.app.domain.model.SketchPrompt

class GetTodayPromptUseCase(private val prompts: List<SketchPrompt>) {

    operator fun invoke(pack: PromptPack, year: Int, dayOfYear: Int): SketchPrompt {
        val pool = if (pack == PromptPack.MIXED) {
            prompts
        } else {
            val filtered = prompts.filter { it.category == pack }
            if (filtered.isEmpty()) prompts else filtered
        }
        if (pool.isEmpty()) {
            return SketchPrompt(0, FALLBACK_TEXT, PromptPack.MIXED, FALLBACK_HINT)
        }
        val index = (((year * 1000L) + dayOfYear) % pool.size).toInt()
        val safeIndex = if (index < 0) index + pool.size else index
        return pool[safeIndex]
    }

    private companion object {
        const val FALLBACK_TEXT = "Draw the first thing you see when you look up."
        const val FALLBACK_HINT = "Ten lines, no more."
    }
}
