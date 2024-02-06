package com.chummer.finance.ui.screens.card

import com.chummer.finance.ui.account.AccountUiModel
import com.chummer.finance.ui.account.DayWithTransactions
import com.chummer.finance.ui.transaction.SearchBarState
import kotlinx.collections.immutable.ImmutableList

data class CardUiState(
    val account: AccountUiModel,
    val searchBarState: SearchBarState,
    val daysWithTransactions: ImmutableList<DayWithTransactions>
)
