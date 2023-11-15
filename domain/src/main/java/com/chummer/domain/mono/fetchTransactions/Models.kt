package com.chummer.domain.mono.fetchTransactions

import java.time.LocalDateTime

data class FetchTransactionsArgument(
    val accountId: String?,
    val jarId: String?,
    val from: Long,
    val to: Long
)

sealed interface FetchTransactionsResult {
    val fetchFullyCompleted: Boolean

    data class TransactionsReturned(
        override val fetchFullyCompleted: Boolean,
        val recentTransactionDateTime: LocalDateTime,
        val oldestTransactionDateTime: LocalDateTime
    ) : FetchTransactionsResult

    data object NoTransactionsReturned : FetchTransactionsResult {
        override val fetchFullyCompleted: Boolean = true
    }
}
