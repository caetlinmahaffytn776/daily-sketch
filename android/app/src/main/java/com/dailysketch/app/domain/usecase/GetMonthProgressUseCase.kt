package com.dailysketch.app.domain.usecase

import com.dailysketch.app.domain.model.CalendarDay
import com.dailysketch.app.domain.model.DayState
import com.dailysketch.app.domain.repository.JournalRepository
import java.time.LocalDate

class GetMonthProgressUseCase(private val repository: JournalRepository) {

    operator fun invoke(today: LocalDate): List<CalendarDay> {
        val days = HashSet<String>()
        for (entry in repository.entries()) {
            days.add(entry.dateIso)
        }
        val first = today.withDayOfMonth(1)
        val length = today.lengthOfMonth()
        val result = ArrayList<CalendarDay>(length)
        var index = 1
        while (index <= length) {
            val date = first.withDayOfMonth(index)
            val completed = days.contains(date.toString())
            val state = when {
                date.isEqual(today) && completed -> DayState.TODAY_COMPLETED
                date.isEqual(today) -> DayState.TODAY
                date.isAfter(today) -> DayState.FUTURE
                completed -> DayState.COMPLETED
                else -> DayState.MISSED
            }
            result.add(CalendarDay(index, state))
            index += 1
        }
        return result
    }

    fun leadingBlanks(today: LocalDate): Int = today.withDayOfMonth(1).dayOfWeek.value - 1
}
