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
import androidx.compose.runtime.MutableState
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
import com.chummer.finance.ui.theme.AppTheme
import com.chummer.finance.ui.theme.LocalColors
import com.chummer.finance.utils.ConfigurePaging
import com.chummer.finance.utils.OnPageLoad
import com.chummer.finance.utils.PagingDirection
import com.chummer.finance.utils.toMonthName
import com.chummer.models.mapping.toUnixSecond
import kotlinx.collections.immutable.toImmutableList
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Calendar(
    today: LocalDate,
    onSubmit: (Pair<LocalDate, LocalDate>?) -> Unit
) = Column(
    Modifier.padding(
        bottom = WindowInsets.safeContent.asPaddingValues().calculateBottomPadding().plus(16.dp)
    )
) {

    val onDayClicked: ((LocalDate, Boolean) -> Unit) = remember {
        { date, isSelected ->
            // TODO implement day selection
        }
    }

    WeekHeader()
    DividerView(paddingValues = PaddingValues(0.dp))

    val listState = rememberLazyListState()

    val selectedRangeStartState: MutableState<LocalDate?> = remember { mutableStateOf(null) }
    val selectedRangeEndState: MutableState<LocalDate?> = remember { mutableStateOf(null) }

    val selectedRangeStart by selectedRangeStartState
    val selectedRangeEnd by selectedRangeEndState

    val yearsState = remember {
        mutableStateOf(
            listOf(
                getYear(today.year),
                getYear(today.year - 1)
            ).toImmutableList()
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
            val year = getYear(yearInt)

            val dropStart =
                if (direction is PagingDirection.Forward && pagingConfig.shouldDropYears) 1 else 0
            val dropLast =
                if (direction is PagingDirection.Backward && pagingConfig.shouldDropYears) 1 else 0

            yearsState.value = (years + listOf(year))
                .sortedByDescending { it.year }
                .drop(dropStart)
                .dropLast(dropLast)
                .toImmutableList()
        }
    }

    ConfigurePaging(itemsOffset = 3, listState = listState, updatePages = paging)

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

    DividerView(paddingValues = PaddingValues(bottom = 12.dp))

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

typealias OnDatesSelected = ((Pair<LocalDate, LocalDate>?) -> Unit)
