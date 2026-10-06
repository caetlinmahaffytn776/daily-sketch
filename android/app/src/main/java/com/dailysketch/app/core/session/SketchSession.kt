package com.dailysketch.app.core.session

import com.dailysketch.app.domain.model.JournalStats
import com.dailysketch.app.domain.model.SketchEntry

class SketchSession {

    private var entry: SketchEntry? = null

    private var stats: JournalStats? = null

    private var completed = 0

    fun record(saved: SketchEntry, snapshot: JournalStats) {
        entry = saved
        stats = snapshot
        completed += 1
    }

    fun lastEntry(): SketchEntry? = entry

    fun lastStats(): JournalStats? = stats

    fun completedCount(): Int = completed

    fun reset() {
        entry = null
        stats = null
        completed = 0
    }
}
