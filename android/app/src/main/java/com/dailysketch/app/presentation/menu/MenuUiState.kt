package com.dailysketch.app.presentation.menu

import com.dailysketch.app.domain.model.CalendarDay
import com.dailysketch.app.domain.model.PromptPack

data class MenuUiState(
    val loading: Boolean = true,
    val dateLine: String = "",
    val monthLabel: String = "",
    val promptText: String = "",
    val categoryPack: PromptPack = PromptPack.MIXED,
    val streak: Int = 0,
    val totalEntries: Int = 0,
    val days: List<CalendarDay> = emptyList(),
    val leadingBlanks: Int = 0,
    val todayDone: Boolean = false,
    val reminderMark: Boolean = false
) {

    val showsStats: Boolean
        get() = streak > 0 && totalEntries > 0
}
