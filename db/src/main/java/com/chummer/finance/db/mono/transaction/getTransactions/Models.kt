package com.chummer.finance.db.mono.transaction.getTransactions

data class ListTransactionItem(
    val id: String,
    val time: Long,
    val description: String,
    val operationAmount: Long,
    val currencyCode: Int,
    val mcc: Int,
    val originalMcc: Int,
    val cashback: Long
) {
    val isIncome: Boolean
        get() = operationAmount > 0
}
