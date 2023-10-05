package com.chummer.finance.transaction

data class TransactionUiListModel(
    val name: String,
    val time: String,
    val price: String,
    val income: Boolean,
    val icon: TransactionIconUiModel
)
