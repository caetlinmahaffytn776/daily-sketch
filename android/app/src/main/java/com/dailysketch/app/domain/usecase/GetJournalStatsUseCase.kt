package com.dailysketch.app.domain.usecase

import com.dailysketch.app.domain.model.JournalStats
import com.dailysketch.app.domain.repository.JournalRepository
import java.time.LocalDate

class GetJournalStatsUseCase(
    private val repository: JournalRepository,
    private val calculateStreak: CalculateStreakUseCase
) {

    operator fun invoke(today: LocalDate): JournalStats {
        val entries = repository.entries()
        var minutes = 0
        for (entry in entries) {
            minutes += entry.minutes
        }
        val newest = entries.maxByOrNull { it.savedAtMillis }
        return JournalStats(
            streak = calculateStreak(today),
            totalEntries = entries.size,
            totalMinutes = minutes,
            lastMinutes = newest?.minutes ?: 0
        )
    }
}
