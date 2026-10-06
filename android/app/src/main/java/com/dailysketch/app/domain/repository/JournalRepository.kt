package com.dailysketch.app.domain.repository

import com.dailysketch.app.domain.model.SketchEntry

interface JournalRepository {

    fun entries(): List<SketchEntry>

    fun save(entry: SketchEntry): Boolean

    fun clear()
}
