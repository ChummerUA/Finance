package com.chummer.finance.ui.calendar

import com.chummer.finance.utils.toShortDayName
import com.chummer.finance.utils.withDayOfWeek
import com.chummer.models.mapping.toUnixSecond
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.time.DayOfWeek
import java.time.LocalDate


enum class DaySelectionViewMode {
    Start,
    Middle,
    End,
    SingleDay,
    None
}

fun getYear(
    year: Int
): CalendarYear {
    var current = LocalDate.of(year, 1, 1)
    val today = LocalDate.now()
    val list = mutableListOf<CalendarMonth>()
    while (current.year == year && current.isBefore(today)) {
        list.add(CalendarMonth.fromLocalDate(current))
        current = current.plusMonths(1L)
    }

    return CalendarYear(list.toImmutableList())
}

data class CalendarYear(
    val months: ImmutableList<CalendarMonth>
) {
    val year
        get() = months.first().start.date.year
}

data class CalendarMonth(
    val weeks: ImmutableList<CalendarWeek>
) {
    val start
        get() = weeks.first().days.first { !it.isPlaceholder }

    val key
        get() = weeks.first().key

    companion object {
        fun fromLocalDate(date: LocalDate): CalendarMonth {
            val today = LocalDate.now()
            var current = date.withDayOfMonth(1)
            val weekStarts = mutableListOf<LocalDate>()
            while (current.month == date.month && current.isBefore(today)) {
                weekStarts.add(current)
                current = if (current.dayOfWeek.value == 1)
                    current.plusWeeks(1L)
                else
                    current.withDayOfWeek(1).plusWeeks(1L)
            }
            val weeks = weekStarts.map { CalendarWeek.fromLocalDate(it) }.toImmutableList()
            return CalendarMonth(weeks)
        }
    }
}

data class CalendarWeek(
    val days: ImmutableList<CalendarDate>
) {
    val key
        get() = days.first { !it.isPlaceholder }.key

    companion object {
        fun fromLocalDate(date: LocalDate): CalendarWeek {
            var current = date.withDayOfWeek(1)

            val days = mutableListOf<CalendarDate>()

            fun isPlaceholder() = current.month != date.month || current.isAfter(LocalDate.now())

            fun addAndIncrease() {
                days.add(CalendarDate(current, DaySelectionViewMode.None, isPlaceholder()))
                current = current.plusDays(1)
            }

            addAndIncrease()
            while (current.dayOfWeek.value != 1) {
                addAndIncrease()
            }

            return CalendarWeek(days.toImmutableList())
        }
    }
}

data class CalendarDate(
    val date: LocalDate,
    var selectionMode: DaySelectionViewMode,
    val isPlaceholder: Boolean
) {
    val key
        get() = date.toUnixSecond()
}

fun getWeekDayNames(): List<String> {
    val firstDay = DayOfWeek.of(1)
    val start = LocalDate.now().withDayOfWeek(firstDay)
    var current = start
    val list = mutableListOf<String>()
    fun addAndIncrease() {
        list.add(current.toShortDayName())
        current = current.plusDays(1L)
    }
    addAndIncrease()
    while (current.dayOfWeek != firstDay) {
        addAndIncrease()
    }
    return list
}

data class CalendarPagingConfig(
    val currentListSize: Int,
    val nextYear: Int,
    val previousYear: Int
) {
    val shouldDropYears = currentListSize > 2

    companion object {
        fun fromYears(years: List<CalendarYear>) = CalendarPagingConfig(
            currentListSize = years.size,
            nextYear = years.last().year - 1,
            previousYear = years.first().year + 1,
        )
    }
}
