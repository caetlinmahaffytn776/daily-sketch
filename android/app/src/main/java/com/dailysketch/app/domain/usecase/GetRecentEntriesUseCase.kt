package com.dailysketch.app.domain.usecase

import com.dailysketch.app.domain.model.SketchEntry
import com.dailysketch.app.domain.repository.JournalRepository

class GetRecentEntriesUseCase(private val repository: JournalRepository) {

    operator fun invoke(limit: Int): List<SketchEntry> {
        val sorted = repository.entries().sortedWith(
            compareByDescending<SketchEntry> { it.dateIso }.thenByDescending { it.savedAtMillis }
        )
        if (sorted.size <= limit) return sorted
        return sorted.subList(0, limit)
    }
}
