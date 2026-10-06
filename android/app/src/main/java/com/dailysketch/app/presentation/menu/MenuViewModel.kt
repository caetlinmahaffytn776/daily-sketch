package com.dailysketch.app.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailysketch.app.core.session.SketchSession
import com.dailysketch.app.core.util.DateFormatter
import com.dailysketch.app.domain.model.DayState
import com.dailysketch.app.domain.repository.SettingsRepository
import com.dailysketch.app.domain.usecase.GetJournalStatsUseCase
import com.dailysketch.app.domain.usecase.GetMonthProgressUseCase
import com.dailysketch.app.domain.usecase.GetTodayPromptUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class MenuViewModel(
    private val todayPrompt: GetTodayPromptUseCase,
    private val journalStats: GetJournalStatsUseCase,
    private val monthProgress: GetMonthProgressUseCase,
    private val settings: SettingsRepository,
    private val session: SketchSession
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())

    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val today = LocalDate.now()
            val snapshot = withContext(Dispatchers.IO) {
                val pack = settings.promptPack
                val prompt = todayPrompt(pack, today.year, today.dayOfYear)
                val stats = journalStats(today)
                val days = monthProgress(today)
                val blanks = monthProgress.leadingBlanks(today)
                settings.lastOpenedIso = today.toString()
                MenuUiState(
                    loading = false,
                    dateLine = DateFormatter.headline(today),
                    monthLabel = DateFormatter.month(today),
                    promptText = prompt.text,
                    categoryPack = prompt.category,
                    streak = stats.streak,
                    totalEntries = stats.totalEntries,
                    days = days,
                    leadingBlanks = blanks,
                    todayDone = days.any { it.state == DayState.TODAY_COMPLETED },
                    reminderMark = settings.reminderMark
                )
            }
            _state.value = snapshot
        }
    }

    fun clearSession() {
        session.reset()
    }
}
