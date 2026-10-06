package com.dailysketch.app.data.repository

import com.dailysketch.app.data.local.JournalStorage
import com.dailysketch.app.domain.model.SketchEntry
import com.dailysketch.app.domain.repository.JournalRepository

class JournalRepositoryImpl(private val storage: JournalStorage) : JournalRepository {

    override fun entries(): List<SketchEntry> = try {
        storage.readEntries()
    } catch (e: Exception) {
        emptyList()
    }

    override fun save(entry: SketchEntry): Boolean = try {
        val current = ArrayList(storage.readEntries())
        val existing = current.indexOfFirst {
            it.dateIso == entry.dateIso && it.promptId == entry.promptId
        }
        if (existing >= 0) {
            current[existing] = entry
        } else {
            current.add(entry)
        }
        current.sortWith(
            compareBy<SketchEntry> { it.dateIso }.thenBy { it.savedAtMillis }
        )
        storage.writeEntries(current)
        true
    } catch (e: Exception) {
        false
    }

    override fun clear() {
        try {
            storage.clear()
        } catch (e: Exception) {
            storage.writeEntries(emptyList())
        }
    }
}
