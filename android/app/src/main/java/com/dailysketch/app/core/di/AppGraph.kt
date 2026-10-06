package com.dailysketch.app.core.di

import android.content.Context
import com.dailysketch.app.core.session.SketchSession
import com.dailysketch.app.data.local.JournalStorage
import com.dailysketch.app.data.local.SettingsStorage
import com.dailysketch.app.data.repository.JournalRepositoryImpl
import com.dailysketch.app.data.repository.SettingsRepositoryImpl
import com.dailysketch.app.data.sample.SampleData
import com.dailysketch.app.domain.repository.JournalRepository
import com.dailysketch.app.domain.repository.SettingsRepository
import com.dailysketch.app.domain.usecase.CalculateStreakUseCase
import com.dailysketch.app.domain.usecase.GetJournalStatsUseCase
import com.dailysketch.app.domain.usecase.GetMonthProgressUseCase
import com.dailysketch.app.domain.usecase.GetRecentEntriesUseCase
import com.dailysketch.app.domain.usecase.GetTodayPromptUseCase
import com.dailysketch.app.domain.usecase.SaveEntryUseCase

class AppGraph(context: Context) {

    private val journalStorage: JournalStorage = JournalStorage(context)

    private val settingsStorage: SettingsStorage = SettingsStorage(context)

    private val journalRepository: JournalRepository = JournalRepositoryImpl(journalStorage)

    val settings: SettingsRepository = SettingsRepositoryImpl(settingsStorage)

    val session: SketchSession = SketchSession()

    private val calculateStreak: CalculateStreakUseCase = CalculateStreakUseCase(journalRepository)

    val todayPrompt: GetTodayPromptUseCase = GetTodayPromptUseCase(SampleData.PROMPTS)

    val saveEntry: SaveEntryUseCase = SaveEntryUseCase(journalRepository)

    val journalStats: GetJournalStatsUseCase =
        GetJournalStatsUseCase(journalRepository, calculateStreak)

    val monthProgress: GetMonthProgressUseCase = GetMonthProgressUseCase(journalRepository)

    val recentEntries: GetRecentEntriesUseCase = GetRecentEntriesUseCase(journalRepository)

    fun journal(): JournalRepository = journalRepository
}
