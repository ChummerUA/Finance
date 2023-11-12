package com.chummer.finance.db.mono.transaction.getTransaction

data class GetTransactionsArgument(
    val accountId: String?,
    val jarId: String?,
    val time: Long,
    val pageSize: Int
)
