package com.dailysketch.app.presentation.game

import com.dailysketch.app.core.config.GameConfig
import com.dailysketch.app.domain.model.Mood

data class GameUiState(
    val promptText: String = "",
    val elapsedSeconds: Int = 0,
    val mood: Mood = Mood.NONE,
    val minutes: Int = GameConfig.DEFAULT_MINUTES,
    val notes: String = "",
    val saving: Boolean = false,
    val error: Boolean = false,
    val finished: Boolean = false
) {

    val canSave: Boolean
        get() = !saving && !finished

    val hasNotes: Boolean
        get() = notes.trim().isNotEmpty()
}
