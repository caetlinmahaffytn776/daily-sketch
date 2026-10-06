package com.dailysketch.app.core.ui

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.dailysketch.app.R
import com.dailysketch.app.databinding.ViewPromptCalendarBinding
import com.dailysketch.app.domain.model.CalendarDay
import com.dailysketch.app.domain.model.DayState

class PromptCalendarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding =
        ViewPromptCalendarBinding.inflate(LayoutInflater.from(context), this)

    private val cellSize = resources.getDimensionPixelSize(R.dimen.calendar_cell)

    private val dotSize = resources.getDimensionPixelSize(R.dimen.calendar_dot)

    private val dotSmall = resources.getDimensionPixelSize(R.dimen.calendar_dot_small)

    init {
        orientation = VERTICAL
    }

    fun bind(days: List<CalendarDay>, leadingBlanks: Int) {
        val grid = binding.calendarGrid
        grid.removeAllViews()
        val cells = ArrayList<CalendarDay?>()
        var blank = 0
        while (blank < leadingBlanks) {
            cells.add(null)
            blank += 1
        }
        cells.addAll(days)
        while (cells.size % COLUMNS != 0) {
            cells.add(null)
        }
        var index = 0
        while (index < cells.size) {
            val row = LinearLayout(context)
            row.orientation = HORIZONTAL
            row.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            var column = 0
            while (column < COLUMNS) {
                row.addView(buildCell(cells[index + column]))
                column += 1
            }
            grid.addView(row)
            index += COLUMNS
        }
    }

    private fun buildCell(day: CalendarDay?): View {
        val cell = FrameLayout(context)
        val params = LinearLayout.LayoutParams(0, cellSize, 1f)
        cell.layoutParams = params
        if (day == null) {
            cell.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
            return cell
        }
        if (day.state == DayState.TODAY || day.state == DayState.TODAY_COMPLETED) {
            cell.background = ContextCompat.getDrawable(context, R.drawable.shape_cell_today)
        }
        val dot = View(context)
        val size = when (day.state) {
            DayState.FUTURE -> dotSmall
            else -> dotSize
        }
        val dotParams = FrameLayout.LayoutParams(size, size)
        dotParams.gravity = Gravity.CENTER
        dot.layoutParams = dotParams
        dot.background = ContextCompat.getDrawable(context, dotDrawable(day.state))
        dot.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
        cell.addView(dot)
        cell.contentDescription = describe(day)
        return cell
    }

    private fun dotDrawable(state: DayState): Int = when (state) {
        DayState.COMPLETED -> R.drawable.shape_dot_filled
        DayState.TODAY_COMPLETED -> R.drawable.shape_dot_filled
        DayState.MISSED -> R.drawable.shape_dot_ring
        DayState.TODAY -> R.drawable.shape_dot_ring
        DayState.FUTURE -> R.drawable.shape_dot_future
    }

    private fun describe(day: CalendarDay): String = when (day.state) {
        DayState.COMPLETED -> context.getString(R.string.cd_calendar_done, day.dayOfMonth)
        DayState.TODAY_COMPLETED -> context.getString(R.string.cd_calendar_done, day.dayOfMonth)
        DayState.TODAY -> context.getString(R.string.cd_calendar_today, day.dayOfMonth)
        DayState.MISSED -> context.getString(R.string.cd_calendar_missed, day.dayOfMonth)
        DayState.FUTURE -> context.getString(R.string.cd_calendar_future, day.dayOfMonth)
    }

    private companion object {
        const val COLUMNS = 7
    }
}
