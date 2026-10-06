package com.dailysketch.app.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailysketch.app.core.config.GameConfig
import com.dailysketch.app.core.session.SketchSession
import com.dailysketch.app.domain.model.Mood
import com.dailysketch.app.domain.model.SketchEntry
import com.dailysketch.app.domain.model.SketchPrompt
import com.dailysketch.app.domain.repository.SettingsRepository
import com.dailysketch.app.domain.usecase.GetJournalStatsUseCase
import com.dailysketch.app.domain.usecase.GetTodayPromptUseCase
import com.dailysketch.app.domain.usecase.SaveEntryUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class GameViewModel(
    private val todayPrompt: GetTodayPromptUseCase,
    private val saveEntry: SaveEntryUseCase,
    private val journalStats: GetJournalStatsUseCase,
    private val settings: SettingsRepository,
    private val session: SketchSession
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())

    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private var tickJob: Job? = null

    private var backstopJob: Job? = null

    private var saveJob: Job? = null

    private var prompt: SketchPrompt? = null

    private var interacted = false

    private var graceUsed = false

    init {
        loadPrompt()
        startTicking()
        armBackstop()
    }

    private fun loadPrompt() {
        viewModelScope.launch {
            val today = LocalDate.now()
            val loaded = withContext(Dispatchers.IO) {
                todayPrompt(
                    settings.promptPack,
                    today.year,
                    today.dayOfYear + session.completedCount()
                )
            }
            prompt = loaded
            _state.value = _state.value.copy(promptText = loaded.text)
        }
    }

    private fun startTicking() {
        tickJob?.cancel()
        tickJob = viewModelScope.launch {
            while (isActive) {
                delay(GameConfig.SESSION_TICK_MS)
                val current = _state.value
                if (current.finished) return@launch
                _state.value = current.copy(elapsedSeconds = current.elapsedSeconds + 1)
            }
        }
    }

    private fun armBackstop() {
        backstopJob?.cancel()
        backstopJob = viewModelScope.launch {
            delay(GameConfig.IDLE_AUTOSAVE_MS)
            if (interacted && !graceUsed) {
                graceUsed = true
                interacted = false
                delay(GameConfig.IDLE_GRACE_MS)
            }
            save()
        }
    }

    fun selectMood(mood: Mood) {
        interacted = true
        _state.value = _state.value.copy(mood = mood, error = false)
    }

    fun selectMinutes(minutes: Int) {
        interacted = true
        _state.value = _state.value.copy(minutes = minutes, error = false)
    }

    fun updateNotes(value: String) {
        interacted = true
        val trimmed = if (value.length > GameConfig.NOTES_MAX_LENGTH) {
            value.substring(0, GameConfig.NOTES_MAX_LENGTH)
        } else {
            value
        }
        _state.value = _state.value.copy(notes = trimmed)
    }

    fun save() {
        val current = _state.value
        if (!current.canSave) return
        if (saveJob != null) return
        _state.value = current.copy(saving = true, error = false)
        saveJob = viewModelScope.launch {
            delay(GameConfig.SAVE_DELAY_MS)
            val today = LocalDate.now()
            val source = prompt
            val entry = SketchEntry(
                dateIso = today.toString(),
                promptId = source?.id ?: 0,
                promptText = source?.text.orEmpty(),
                mood = _state.value.mood,
                notes = _state.value.notes.trim(),
                minutes = _state.value.minutes,
                savedAtMillis = System.currentTimeMillis()
            )
            val stored = withContext(Dispatchers.IO) {
                val ok = saveEntry(entry)
                if (ok) journalStats(today) else null
            }
            saveJob = null
            if (stored == null) {
                _state.value = _state.value.copy(saving = false, error = true)
                return@launch
            }
            session.record(entry, stored)
            tickJob?.cancel()
            backstopJob?.cancel()
            _state.value = _state.value.copy(saving = false, error = false, finished = true)
        }
    }

    fun consumeFinish() {
        _state.value = _state.value.copy(finished = false)
    }

    override fun onCleared() {
        tickJob?.cancel()
        backstopJob?.cancel()
        saveJob?.cancel()
        tickJob = null
        backstopJob = null
        saveJob = null
        super.onCleared()
    }
}
