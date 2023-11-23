package com.chummer.finance.ui.screens.card

import com.chummer.finance.ui.account.AccountUiModel
import com.chummer.finance.ui.account.DayWithTransactions
import kotlinx.collections.immutable.ImmutableList

data class CardUiState(
    val account: AccountUiModel,
    val daysWithTransactions: ImmutableList<DayWithTransactions>
)
