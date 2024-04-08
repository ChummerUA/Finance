package com.chummer.finance.ui.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.chummer.finance.ui.Divider
import com.chummer.finance.ui.button.PrimaryButton
import com.chummer.finance.ui.spacing.Space
import com.chummer.finance.ui.text.ItemDescriptionText
import com.chummer.finance.ui.text.ItemTitleText
import com.chummer.finance.ui.theme.AppTheme
import com.chummer.finance.ui.theme.LocalColors
import com.chummer.finance.utils.ConfigurePaging
import com.chummer.finance.utils.OnPageLoad
import com.chummer.finance.utils.PagingDirection
import com.chummer.finance.utils.TypedOnClickListener
import com.chummer.finance.utils.toMonthName
import com.chummer.models.mapping.toUnixSecond
import kotlinx.collections.immutable.toImmutableList
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Calendar(
    today: LocalDate,
    range: Pair<LocalDate, LocalDate>?,
    onSubmit: (Pair<LocalDate, LocalDate>?) -> Unit
) = Column(
    Modifier.padding(
        bottom = WindowInsets.safeContent.asPaddingValues().calculateBottomPadding().plus(16.dp)
    )
) {
    WeekHeader()
    Divider(paddingValues = PaddingValues(0.dp))

    val listState = rememberLazyListState()

    val selectedRangeStartState = remember { mutableStateOf(range?.first) }
    val selectedRangeEndState = remember { mutableStateOf(range?.second) }

    val selectedRangeStart by selectedRangeStartState
    val selectedRangeEnd by selectedRangeEndState

    val yearsState = remember {
        mutableStateOf(
            getYearsForCalendar(
                listOf(today.year, today.year - 1),
                selectedRangeStart,
                selectedRangeEnd
            )
        )
    }
    val years by yearsState

    val pagingConfig by remember { derivedStateOf { CalendarPagingConfig.fromYears(years) } }
    val paging: OnPageLoad = remember(pagingConfig) {
        { direction ->
            val yearInt = when (direction) {
                PagingDirection.Forward -> pagingConfig.nextYear
                PagingDirection.Backward -> pagingConfig.previousYear
            }
            val year = if (yearInt <= LocalDate.now().year)
                getYearForCalendar(yearInt, selectedRangeStart, selectedRangeEnd)
            else null

            val dropStart =
                if (direction is PagingDirection.Forward && pagingConfig.shouldDropYears) 1 else 0
            val dropLast =
                if (direction is PagingDirection.Backward && pagingConfig.shouldDropYears) 1 else 0

            if (year != null)
                yearsState.value = (years + listOf(year))
                    .sortedByDescending { it.year }
                    .drop(dropStart)
                    .dropLast(dropLast)
                    .toImmutableList()
        }
    }

    ConfigurePaging(itemsOffset = 3, listState = listState, updatePages = paging)

    val onDayClicked: TypedOnClickListener<LocalDate, Unit> =
        remember(selectedRangeStart, selectedRangeEnd) {
            { date ->
                val (newStart, newEnd) = when {
                    selectedRangeStart == null -> date to date
                    selectedRangeStart != null && selectedRangeEnd != null && selectedRangeStart != selectedRangeEnd -> date to date
                    selectedRangeStart?.isBefore(date) == true -> selectedRangeStart to date
                    selectedRangeStart?.isAfter(date) == true -> date to selectedRangeStart
                    else -> null to null
                }

                selectedRangeStartState.value = newStart
                selectedRangeEndState.value = newEnd

                yearsState.value = getYearsForCalendar(
                    years.map { it.year },
                    newStart,
                    newEnd
                )
            }
        }

    LazyColumn(
        reverseLayout = true,
        state = listState,
        modifier = Modifier.weight(1f)
    ) {
        for (year in years) {
            stickyHeader {
                YearHeader(year = year.year)
            }
            items(year.months, key = { it.key }) {
                Month(month = it, onDayClicked)
            }
        }
    }

    Divider(paddingValues = PaddingValues(bottom = 12.dp))

    val onClick = remember(selectedRangeStart, selectedRangeEnd) {
        {
            if (selectedRangeStart != null && selectedRangeEnd != null)
                onSubmit(selectedRangeStart!! to selectedRangeEnd!!)
            else
                onSubmit(null)
        }
    }
    PrimaryButton(text = stringResource(id = R.string.show), onClick = onClick)
}

@Composable
private fun YearHeader(
    year: Int
) = Row(
    modifier = Modifier
        .fillMaxWidth(1f)
        .padding(vertical = 4.dp),
    horizontalArrangement = Arrangement.Center
) {
    ItemTitleText(
        text = year.toString(),
        color = AppTheme.colors.textPrimary,
        modifier = Modifier
            .background(
                color = AppTheme.colors.backgroundSecondary,
                shape = RoundedCornerShape(percent = 50)
            )
            .padding(
                horizontal = 12.dp,
                vertical = 8.dp
            )
    )
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
private fun RowScope.HeaderDay(dayName: String) {
    val onClick = remember { { } }
    DayContainer(onClick = onClick, clickable = false, shape = RectangleShape, shapeColor = null) {
        ItemDescriptionText(text = dayName, color = LocalColors.current.textPrimary)
    }
}

@Composable
private fun Month(
    month: CalendarMonth,
    onDayClicked: TypedOnClickListener<LocalDate, Unit>
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
private fun Week(week: CalendarWeek, onDayClicked: TypedOnClickListener<LocalDate, Unit>) = Row(
    Modifier
        .fillMaxWidth(1f)
        .padding(horizontal = 8.dp, vertical = 4.dp)
) {
    week.days.forEach {
        key(it.key) {
            Day(it, onDayClicked = onDayClicked)
        }
    }
}

@Composable
private fun RowScope.Day(
    day: CalendarDate,
    onDayClicked: TypedOnClickListener<LocalDate, Unit>
) {
    val colors = LocalColors.current
    val isSelected by remember(day.key, day.selectionMode) {
        derivedStateOf { day.selectionMode != DaySelectionMode.None }
    }

    val text by remember {
        derivedStateOf { if (day.isPlaceholder) "" else day.date.dayOfMonth.toString() }
    }
    val textColor = if (isSelected) colors.onPrimary
    else colors.textPrimary

    val backgroundColor = if (isSelected) colors.primary
    else null

    val shape by remember(day.selectionMode) {
        derivedStateOf {
            when (day.selectionMode) {
                DaySelectionMode.Start -> RoundedCornerShape(
                    topStartPercent = 50,
                    bottomStartPercent = 50
                )

                DaySelectionMode.End -> RoundedCornerShape(
                    topEndPercent = 50,
                    bottomEndPercent = 50
                )

                DaySelectionMode.SingleDay -> CircleShape
                else -> RectangleShape
            }
        }
    }

    val onClick = remember(day.date.toUnixSecond()) {
        { onDayClicked(day.date) }
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

// These nested boxes are disgusting
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
        .height(dayContainerHeight)
        .let { modifier ->
            if (shape == CircleShape)
                modifier.aspectRatio(1f, true)
            else modifier
        }
        .weight(1f)
        .let { modifier -> shapeColor?.let { modifier.background(it, shape) } ?: modifier }
) {
    Box(
        contentAlignment = Alignment.Center,
        content = content,
        modifier = Modifier
            .height(dayContainerHeight)
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
            .clickable(enabled = clickable, onClick = onClick)
    )
}

typealias OnDatesSelected = ((Pair<LocalDate, LocalDate>?) -> Unit)

private val dayContainerHeight = 36.dp
