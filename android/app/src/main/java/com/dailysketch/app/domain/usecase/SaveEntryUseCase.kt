package com.dailysketch.app.domain.usecase

import com.dailysketch.app.domain.model.SketchEntry
import com.dailysketch.app.domain.repository.JournalRepository

class SaveEntryUseCase(private val repository: JournalRepository) {

    operator fun invoke(entry: SketchEntry): Boolean = repository.save(entry)
}
