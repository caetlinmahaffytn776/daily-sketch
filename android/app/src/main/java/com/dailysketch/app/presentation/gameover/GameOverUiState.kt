package com.dailysketch.app.presentation.gameover

import com.dailysketch.app.domain.model.SketchEntry

data class GameOverUiState(
    val loading: Boolean = true,
    val streak: Int = 0,
    val totalEntries: Int = 0,
    val lastMinutes: Int = 0,
    val notes: String = "",
    val promptText: String = "",
    val entries: List<SketchEntry> = emptyList()
) {

    val hasNotes: Boolean
        get() = notes.trim().isNotEmpty()

    val hasPrompt: Boolean
        get() = promptText.trim().isNotEmpty()
}
