package com.chummer.finance.db.mono.transaction.getTransaction

data class GetTransactionsArgument(
    val accountId: String?,
    val time: Long,
    val pageSize: Long,
    val pages: Long,
    val shiftOnePageBack: Boolean = false
)
