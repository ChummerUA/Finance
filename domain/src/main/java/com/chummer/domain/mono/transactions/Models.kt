package com.chummer.domain.mono.transactions

data class FetchTransactionsArgument(
    val accountId: String?,
    val jarId: String?,
    val from: Long,
    val to: Long
)
