package com.dailysketch.app.core.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateFormatter {

    private val headline: DateTimeFormatter =
        DateTimeFormatter.ofPattern("EEE · dd MMM", Locale.ENGLISH)

    private val compact: DateTimeFormatter =
        DateTimeFormatter.ofPattern("dd MMM", Locale.ENGLISH)

    private val month: DateTimeFormatter =
        DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH)

    fun headline(date: LocalDate): String = headline.format(date)

    fun compact(value: String): String = try {
        compact.format(LocalDate.parse(value)).uppercase(Locale.ENGLISH)
    } catch (e: Exception) {
        value
    }

    fun month(date: LocalDate): String = month.format(date).uppercase(Locale.ENGLISH)

    fun elapsed(seconds: Int): String {
        val safe = if (seconds < 0) 0 else seconds
        val minutes = safe / 60
        val rest = safe % 60
        return String.format(Locale.ENGLISH, "%02d:%02d", minutes, rest)
    }
}
