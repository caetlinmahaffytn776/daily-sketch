package com.dailysketch.app.domain.model

data class JournalStats(
    val streak: Int,
    val totalEntries: Int,
    val totalMinutes: Int,
    val lastMinutes: Int
)
