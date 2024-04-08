package com.chummer.finance.ui.transaction

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chummer.finance.R
import com.chummer.finance.ui.IconState
import com.chummer.finance.ui.IconView
import com.chummer.finance.ui.spacing.Space
import com.chummer.finance.ui.text.FilledInputTextView
import com.chummer.finance.ui.text.defaultFilledInputTextViewModifier
import com.chummer.finance.utils.OnClickListener
import com.chummer.finance.utils.OnTextChanged

@Composable
fun TransactionsSearchBar(
    state: SearchBarState,
    onSearchClicked: OnClickListener,
    onCategoriesClicked: OnClickListener,
    onCalendarClicked: OnClickListener,
    onCancelClicked: OnClickListener,
    onTextChanged: OnTextChanged
) = when (state) {
    SearchBarState.Default -> DefaultSearchBar(onSearchClicked)
    is SearchBarState.Expanded -> ExpandedSearchBar(
        calendarIconState = state.calendarIconState,
        categoriesIconState = state.categoriesIconState,
        text = state.search,
        onCategoriesClicked = onCategoriesClicked,
        onCalendarClicked = onCalendarClicked,
        onCancelClicked = onCancelClicked,
        onTextChanged = onTextChanged
    )
}

//region base components
@Composable
private fun SearchContainer(content: @Composable (RowScope.() -> Unit)) = Row(
    modifier = Modifier
        .padding(
            horizontal = 16.dp,
            vertical = 4.dp
        )
        .defaultMinSize(minHeight = 34.dp),
    verticalAlignment = Alignment.CenterVertically,
    content = content
)

@Composable
private fun RowScope.SearchBarSpacing() = Space(size = 8.dp)
//endregion

//region states
@Composable
private fun DefaultSearchBar(
    onSearchClicked: OnClickListener
) = SearchContainer {
    val onTextChanged: OnTextChanged = remember { { } }
    FilledInputTextView(
        text = "",
        onTextChanged = onTextChanged,
        modifier = Modifier
            .alpha(0f)
            .weight(1f)
            .defaultFilledInputTextViewModifier()
    )
    SearchButton(onSearchClicked)
}

@Composable
private fun ExpandedSearchBar(
    calendarIconState: IconState,
    categoriesIconState: IconState,
    text: String,
    onCategoriesClicked: OnClickListener,
    onCalendarClicked: OnClickListener,
    onCancelClicked: OnClickListener,
    onTextChanged: OnTextChanged
) = SearchContainer {

    val focusManager = LocalFocusManager.current
    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(focusRequester, calendarIconState, categoriesIconState) {
        if (calendarIconState.selected || categoriesIconState.selected)
            focusManager.clearFocus()
        else
            focusRequester.requestFocus()
    }

    CategoriesButton(
        state = categoriesIconState,
        onClicked = onCategoriesClicked
    )
    SearchBarSpacing()

    CalendarButton(
        state = calendarIconState,
        onClicked = onCalendarClicked
    )
    SearchBarSpacing()

    FilledInputTextView(
        text = text,
        onTextChanged = onTextChanged,
        modifier = Modifier
            .weight(1f)
            .defaultFilledInputTextViewModifier()
            .focusRequester(focusRequester = focusRequester)
    )

    SearchBarSpacing()
    CancelButton(
        onClicked = onCancelClicked
    )
}
//endregion

//region icons
@Composable
private fun CategoriesButton(
    state: IconState = IconState.Default,
    onClicked: OnClickListener
) = IconView(
    id = R.drawable.ic_categories,
    contentDescription = stringResource(id = R.string.transactions_search_bar_categories_icon_description),
    state = state,
    size = 24.dp,
    onClicked = onClicked
)

@Composable
private fun CalendarButton(
    state: IconState = IconState.Default,
    onClicked: OnClickListener
) = IconView(
    id = R.drawable.ic_calendar,
    contentDescription = stringResource(id = R.string.transactions_search_bar_calendar_icon_description),
    state = state,
    size = 24.dp,
    onClicked = onClicked
)

@Composable
private fun SearchButton(
    onSearchClicked: OnClickListener
) = IconView(
    id = R.drawable.ic_search,
    contentDescription = stringResource(id = R.string.transactions_search_bar_search_icon_description),
    state = IconState.Default,
    size = 24.dp,
    onClicked = onSearchClicked
)

@Composable
private fun CancelButton(
    state: IconState = IconState.Default,
    onClicked: OnClickListener
) = IconView(
    id = R.drawable.ic_cancel,
    contentDescription = stringResource(id = R.string.transactions_search_bar_cancel_icon_description),
    state = state,
    size = 24.dp,
    onClicked = onClicked
)
//endregion
