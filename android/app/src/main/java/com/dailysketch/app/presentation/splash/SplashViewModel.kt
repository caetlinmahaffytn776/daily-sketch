package com.dailysketch.app.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailysketch.app.core.config.GameConfig
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val _state = MutableStateFlow(
        SplashUiState(
            elapsedMillis = 0L,
            totalMillis = GameConfig.LOADER_DURATION_MS,
            preparing = true,
            readyToAdvance = false
        )
    )

    val state: StateFlow<SplashUiState> = _state.asStateFlow()

    private var timerJob: Job? = null

    private var handedOver = false

    fun start() {
        if (handedOver) return
        if (timerJob != null) return
        timerJob = viewModelScope.launch {
            val startedAt = System.currentTimeMillis()
            var elapsed = 0L
            while (isActive && elapsed < GameConfig.LOADER_DURATION_MS) {
                delay(GameConfig.SPLASH_TICK_MS)
                elapsed = System.currentTimeMillis() - startedAt
                _state.value = _state.value.copy(
                    elapsedMillis = capped(elapsed),
                    preparing = true
                )
            }
            _state.value = _state.value.copy(
                elapsedMillis = GameConfig.LOADER_DURATION_MS,
                preparing = false,
                readyToAdvance = true
            )
        }
    }

    fun consumeAdvance() {
        handedOver = true
        timerJob?.cancel()
        timerJob = null
        _state.value = _state.value.copy(readyToAdvance = false)
    }

    fun hasHandedOver(): Boolean = handedOver

    private fun capped(value: Long): Long =
        if (value > GameConfig.LOADER_DURATION_MS) GameConfig.LOADER_DURATION_MS else value

    override fun onCleared() {
        timerJob?.cancel()
        timerJob = null
        super.onCleared()
    }
}
