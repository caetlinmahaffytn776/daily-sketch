package com.dailysketch.app.data.local

import android.content.Context
import android.content.SharedPreferences
import com.dailysketch.app.data.sample.SampleData
import com.dailysketch.app.domain.model.Mood
import com.dailysketch.app.domain.model.SketchEntry
import java.time.LocalDate

class JournalStorage(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun readEntries(): List<SketchEntry> {
        ensureSeeded()
        val raw = preferences.getString(KEY_ENTRIES, "").orEmpty()
        return decode(raw)
    }

    fun writeEntries(entries: List<SketchEntry>) {
        preferences.edit().putString(KEY_ENTRIES, encode(entries)).apply()
    }

    fun clear() {
        preferences.edit()
            .putString(KEY_ENTRIES, "")
            .putBoolean(KEY_SEEDED, true)
            .apply()
    }

    private fun ensureSeeded() {
        if (preferences.getBoolean(KEY_SEEDED, false)) return
        val seeded = SampleData.seedEntries(LocalDate.now())
        preferences.edit()
            .putString(KEY_ENTRIES, encode(seeded))
            .putBoolean(KEY_SEEDED, true)
            .apply()
    }

    private fun encode(entries: List<SketchEntry>): String {
        val builder = StringBuilder()
        for (entry in entries) {
            if (builder.isNotEmpty()) builder.append(RECORD_SEPARATOR)
            builder.append(entry.dateIso)
            builder.append(UNIT_SEPARATOR)
            builder.append(entry.promptId)
            builder.append(UNIT_SEPARATOR)
            builder.append(sanitize(entry.promptText))
            builder.append(UNIT_SEPARATOR)
            builder.append(entry.mood.id)
            builder.append(UNIT_SEPARATOR)
            builder.append(sanitize(entry.notes))
            builder.append(UNIT_SEPARATOR)
            builder.append(entry.minutes)
            builder.append(UNIT_SEPARATOR)
            builder.append(entry.savedAtMillis)
        }
        return builder.toString()
    }

    private fun decode(raw: String): List<SketchEntry> {
        if (raw.isEmpty()) return emptyList()
        val result = ArrayList<SketchEntry>()
        for (record in raw.split(RECORD_SEPARATOR)) {
            if (record.isEmpty()) continue
            val parts = record.split(UNIT_SEPARATOR)
            if (parts.size < FIELD_COUNT) continue
            result.add(
                SketchEntry(
                    dateIso = parts[0],
                    promptId = parts[1].toIntOrNull() ?: 0,
                    promptText = parts[2],
                    mood = Mood.fromId(parts[3]),
                    notes = parts[4],
                    minutes = parts[5].toIntOrNull() ?: 0,
                    savedAtMillis = parts[6].toLongOrNull() ?: 0L
                )
            )
        }
        return result
    }

    private fun sanitize(value: String): String = value
        .replace(RECORD_SEPARATOR, " ")
        .replace(UNIT_SEPARATOR, " ")
        .trim()

    private companion object {
        const val PREFS_NAME = "daily_sketch_prefs"
        const val KEY_ENTRIES = "entries"
        const val KEY_SEEDED = "entries_seeded"
        const val RECORD_SEPARATOR = ""
        const val UNIT_SEPARATOR = ""
        const val FIELD_COUNT = 7
    }
}
