package com.dailysketch.app.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dailysketch.app.presentation.game.GameViewModel
import com.dailysketch.app.presentation.gameover.GameOverViewModel
import com.dailysketch.app.presentation.menu.MenuViewModel
import com.dailysketch.app.presentation.splash.SplashViewModel

class ViewModelFactory(private val graph: AppGraph) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(MenuViewModel::class.java) -> MenuViewModel(
                graph.todayPrompt,
                graph.journalStats,
                graph.monthProgress,
                graph.settings,
                graph.session
            )

            modelClass.isAssignableFrom(GameViewModel::class.java) -> GameViewModel(
                graph.todayPrompt,
                graph.saveEntry,
                graph.journalStats,
                graph.settings,
                graph.session
            )

            modelClass.isAssignableFrom(GameOverViewModel::class.java) -> GameOverViewModel(
                graph.session,
                graph.journalStats,
                graph.recentEntries
            )

            else -> SplashViewModel()
        }
        @Suppress("UNCHECKED_CAST")
        return created as T
    }
}
