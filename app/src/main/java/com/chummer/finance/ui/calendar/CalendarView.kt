package com.chummer.finance.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chummer.finance.R
import com.chummer.finance.ui.DividerView
import com.chummer.finance.ui.button.PrimaryButton
import com.chummer.finance.ui.spacing.Space
import com.chummer.finance.ui.text.ItemDescriptionText
import com.chummer.finance.ui.text.ItemTitleText
import com.chummer.finance.ui.theme.LocalColors
import com.chummer.finance.utils.toMonthName
import com.chummer.finance.utils.toShortDayName
import com.chummer.finance.utils.withDayOfWeek
import com.chummer.models.mapping.toUnixSecond
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun Calendar(
    today: LocalDate,
) = Column(
    Modifier.padding(
        bottom = WindowInsets.safeContent.asPaddingValues().calculateBottomPadding().plus(16.dp)
    )
) {

    val onDayClicked: ((LocalDate, Boolean) -> Unit) = remember {
        { _, _ -> }
    }

    WeekHeader()

    DividerView(paddingValues = PaddingValues(0.dp))

    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        modifier = Modifier.weight(1f)
    ) {
        val startState = mutableStateOf(today)
        val months: List<CalendarMonth> by derivedStateOf {
            (0..12).map {
                val start = startState.value.minusMonths(it.toLong()).withDayOfMonth(
                    if (it == 0) startState.value.dayOfMonth else 1
                )
                CalendarMonth.fromLocalDate(start)
            }.reversed()
        }
        items(months, key = { it.key }) {
            Month(month = it, onDayClicked)
        }
    }

    DividerView(paddingValues = PaddingValues(bottom = 12.dp))

    val onSubmit: (() -> Unit) = remember {
        { }
    }
    PrimaryButton(text = stringResource(id = R.string.show), onClick = onSubmit)
}

@Composable
private fun WeekHeader() = Row {
    val days = remember {
        getWeekDayNames()
    }
    days.forEachIndexed { index, day ->
        key(index) {
            HeaderDay(dayName = day)
        }
    }
}

@Composable
private fun RowScope.HeaderDay(
    dayName: String
) {
    val onClick = remember { { } }
    DayContainer(onClick = onClick, clickable = false, shape = RectangleShape, shapeColor = null) {
        ItemDescriptionText(text = dayName, color = LocalColors.current.textPrimary)
    }
}

@Composable
private fun Month(
    month: CalendarMonth,
    onDayClicked: (LocalDate, Boolean) -> Unit
) = Column(
    modifier = Modifier.padding(vertical = 8.dp)
) {
    ItemTitleText(
        text = month.start.date.toMonthName(),
        color = LocalColors.current.textPrimary,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
    Space(size = 4.dp)

    month.weeks.forEach {
        key(it.key) {
            Week(week = it, onDayClicked = onDayClicked)
        }
    }
}

@Composable
private fun Week(week: CalendarWeek, onDayClicked: (LocalDate, Boolean) -> Unit) = Row {
    week.days.forEach {
        key(it.key) {
            Day(it, onDayClicked = onDayClicked)
        }
    }
}

@Composable
private fun RowScope.Day(
    day: CalendarDate,
    onDayClicked: ((LocalDate, Boolean) -> Unit)
) {
    val colors = LocalColors.current
    val isSelected by remember {
        derivedStateOf { day.selectionMode != DaySelectionViewMode.None }
    }

    val text by remember {
        derivedStateOf { if (day.isPlaceholder) "" else day.date.dayOfMonth.toString() }
    }
    val textColor = if (isSelected) colors.onPrimary
    else colors.textPrimary

    val backgroundColor = if (isSelected) colors.primary
    else null

    val shape by remember {
        derivedStateOf {
            when (day.selectionMode) {
                DaySelectionViewMode.Start -> RoundedCornerShape(
                    topStartPercent = 50,
                    bottomStartPercent = 50
                )

                DaySelectionViewMode.End -> RoundedCornerShape(
                    topEndPercent = 50,
                    bottomEndPercent = 50
                )

                DaySelectionViewMode.SingleDay -> CircleShape
                else -> RectangleShape
            }
        }
    }

    val onClick = remember(day.date.toUnixSecond(), isSelected) {
        { onDayClicked(day.date, isSelected) }
    }

    DayContainer(
        onClick = onClick,
        clickable = !day.isPlaceholder,
        shape = shape,
        shapeColor = backgroundColor
    ) {
        ItemDescriptionText(text = text, color = textColor)
    }
}

@Composable
private fun RowScope.DayContainer(
    onClick: (() -> Unit),
    clickable: Boolean,
    shape: Shape,
    shapeColor: Color?,
    content: @Composable (BoxScope.() -> Unit)
) = Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
        .weight(1f)
        .height(30.dp)
        .apply {
            shapeColor?.let { this.background(it, shape) } ?: this
        }
        .clickable(onClick = onClick, enabled = clickable),
    content = content
)

enum class DaySelectionViewMode {
    Start,
    Middle,
    End,
    SingleDay,
    None
}

private data class CalendarMonth(
    val weeks: List<CalendarWeek>
) {
    val start
        get() = weeks.first().days.first()

    val key
        get() = weeks.first().key

    companion object {
        fun fromLocalDate(date: LocalDate): CalendarMonth {
            var current = date
            val weekStarts = mutableListOf<LocalDate>()
            while (current.month == date.month) {
                weekStarts.add(current)
                val daysUntilNextWeek = 7 - current.dayOfWeek.value + 1
                current = current.plusDays(daysUntilNextWeek.toLong())
            }
            val weeks = weekStarts.map { CalendarWeek.fromLocalDate(it) }
            return CalendarMonth(weeks)
        }
    }
}

private data class CalendarWeek(
    val days: List<CalendarDate>
) {
    val key
        get() = days.first { !it.isPlaceholder }.key

    companion object {
        fun fromLocalDate(date: LocalDate): CalendarWeek {
            val delta = date.dayOfWeek.value.minus(1L)
            var current = date.minusDays(delta)

            val days = mutableListOf<CalendarDate>()

            fun isPlaceholder() = !(current.month == date.month && !current.isBefore(date))
            fun addAndIncrease() {
                days.add(CalendarDate(current, DaySelectionViewMode.None, isPlaceholder()))
                current = current.plusDays(1)
            }

            addAndIncrease()
            while (current.dayOfWeek.value != 1) {
                addAndIncrease()
            }

            return CalendarWeek(days)
        }
    }
}

private data class CalendarDate(
    val date: LocalDate,
    var selectionMode: DaySelectionViewMode,
    val isPlaceholder: Boolean
) {
    val key
        get() = date.toUnixSecond()
}

private fun getWeekDayNames(): List<String> {
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
