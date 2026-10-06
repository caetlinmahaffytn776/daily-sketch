package com.dailysketch.app.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailysketch.app.core.config.GameConfig
import com.dailysketch.app.core.session.SketchSession
import com.dailysketch.app.domain.usecase.GetJournalStatsUseCase
import com.dailysketch.app.domain.usecase.GetRecentEntriesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class GameOverViewModel(
    private val session: SketchSession,
    private val journalStats: GetJournalStatsUseCase,
    private val recentEntries: GetRecentEntriesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState())

    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            val today = LocalDate.now()
            val snapshot = withContext(Dispatchers.IO) {
                val stats = session.lastStats() ?: journalStats(today)
                val entry = session.lastEntry()
                GameOverUiState(
                    loading = false,
                    streak = stats.streak,
                    totalEntries = stats.totalEntries,
                    lastMinutes = entry?.minutes ?: stats.lastMinutes,
                    notes = entry?.notes.orEmpty(),
                    promptText = entry?.promptText.orEmpty(),
                    entries = recentEntries(GameConfig.MAX_GALLERY_ITEMS)
                )
            }
            _state.value = snapshot
        }
    }
}
