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

fun getYearsForCalendar(
    years: List<Int>,
    selectedRangeStart: LocalDate?,
    selectedRangeEnd: LocalDate?
): ImmutableList<CalendarYear> {
    return years.map {
        getYearForCalendar(it, selectedRangeStart, selectedRangeEnd)
    }.toImmutableList()
}

fun getYearForCalendar(
    year: Int,
    selectedRangeStart: LocalDate?,
    selectedRangeEnd: LocalDate?
): CalendarYear {
    var current = LocalDate.of(year, 1, 1)
    val today = LocalDate.now()
    val list = mutableListOf<CalendarMonth>()
    while (current.year == year && current.isBefore(today)) {
        list.add(CalendarMonth.fromLocalDate(current, selectedRangeStart, selectedRangeEnd))
        current = current.plusMonths(1L)
    }

    return CalendarYear(list.reversed().toImmutableList())
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
        fun fromLocalDate(
            date: LocalDate,
            selectedRangeStart: LocalDate?,
            selectedRangeEnd: LocalDate?
        ): CalendarMonth {
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
            val weeks = weekStarts.map {
                CalendarWeek.fromLocalDate(
                    it,
                    selectedRangeStart,
                    selectedRangeEnd
                )
            }.toImmutableList()
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
        fun fromLocalDate(
            date: LocalDate,
            selectedRangeStart: LocalDate?,
            selectedRangeEnd: LocalDate?
        ): CalendarWeek {
            var current = date.withDayOfWeek(1)

            val days = mutableListOf<CalendarDate>()

            fun isPlaceholder() = current.month != date.month || current.isAfter(LocalDate.now())

            fun addAndIncrease() {
                val selection = if (isPlaceholder()) DaySelectionViewMode.None
                else getSelectionModeForDay(current, selectedRangeStart, selectedRangeEnd)

                days.add(CalendarDate(current, selection, isPlaceholder()))
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
    val selectionMode: DaySelectionViewMode,
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

private fun getSelectionModeForDay(
    date: LocalDate,
    selectedRangeStart: LocalDate?,
    selectedRangeEnd: LocalDate?
): DaySelectionViewMode = when {
    date == selectedRangeStart && date == selectedRangeEnd -> DaySelectionViewMode.SingleDay
    date == selectedRangeStart -> DaySelectionViewMode.Start
    date == selectedRangeEnd -> DaySelectionViewMode.End
    selectedRangeStart?.isBefore(date) == true && selectedRangeEnd?.isAfter(date) == true ->
        DaySelectionViewMode.Middle

    else -> DaySelectionViewMode.None
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
