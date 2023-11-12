package com.chummer.finance.ui.transaction

data class TransactionUiListModel(
    val id: String,
    val name: String,
    val time: String,
    val amount: String,
    val income: Boolean,
    val icon: TransactionIconUiModel? = null
)

sealed class TransactionIconUiModel {
    // TODO define icons
}
