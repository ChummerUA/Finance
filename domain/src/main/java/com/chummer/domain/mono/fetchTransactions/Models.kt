package com.chummer.domain.mono.fetchTransactions

import java.time.LocalDateTime

data class FetchTransactionsArgument(
    val accountId: String?,
    val jarId: String?,
    val from: LocalDateTime,
    val to: LocalDateTime
)

sealed interface FetchTransactionsResult {
    val fetchFullyCompleted: Boolean
    val fetchDate: LocalDateTime

    data class TransactionsReturned(
        override val fetchFullyCompleted: Boolean,
        val recentTransactionDateTime: LocalDateTime,
        val oldestTransactionDateTime: LocalDateTime,
        override val fetchDate: LocalDateTime = recentTransactionDateTime
    ) : FetchTransactionsResult

    data class NoTransactionsReturned(
        override val fetchDate: LocalDateTime,
        override val fetchFullyCompleted: Boolean = true
    ) : FetchTransactionsResult
}
