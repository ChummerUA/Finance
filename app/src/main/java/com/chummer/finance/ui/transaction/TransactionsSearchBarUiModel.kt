package com.chummer.finance.ui.transaction

import com.chummer.finance.ui.IconState
import kotlinx.serialization.Serializable

@Serializable
sealed interface SearchBarState {
    val isCalendar
        get() = this is Expanded && calendarIconState.selected

    val isCategorySelection
        get() = this is Expanded && categoriesIconState.selected

    data object Default : SearchBarState

    data class Expanded(
        val search: String = "",
        val categoriesIconState: IconState = IconState.Default,
        val calendarIconState: IconState = IconState.Default
    ) : SearchBarState
}
