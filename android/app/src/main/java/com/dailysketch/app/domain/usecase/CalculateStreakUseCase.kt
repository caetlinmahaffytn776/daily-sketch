package com.dailysketch.app.domain.usecase

import com.dailysketch.app.domain.repository.JournalRepository
import java.time.LocalDate

class CalculateStreakUseCase(private val repository: JournalRepository) {

    operator fun invoke(today: LocalDate): Int {
        val days = HashSet<String>()
        for (entry in repository.entries()) {
            days.add(entry.dateIso)
        }
        if (days.isEmpty()) return 0
        val anchor = when {
            days.contains(today.toString()) -> today
            days.contains(today.minusDays(1).toString()) -> today.minusDays(1)
            else -> return 0
        }
        var streak = 0
        var cursor = anchor
        while (days.contains(cursor.toString()) && streak < MAX_WALK) {
            streak += 1
            cursor = cursor.minusDays(1)
        }
        return streak
    }

    private companion object {
        const val MAX_WALK = 3650
    }
}
