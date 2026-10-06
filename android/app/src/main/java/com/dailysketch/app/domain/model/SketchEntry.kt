package com.dailysketch.app.domain.model

data class SketchEntry(
    val dateIso: String,
    val promptId: Int,
    val promptText: String,
    val mood: Mood,
    val notes: String,
    val minutes: Int,
    val savedAtMillis: Long
)
