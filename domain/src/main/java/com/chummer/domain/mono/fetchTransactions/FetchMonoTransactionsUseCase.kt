package com.chummer.domain.mono.fetchTransactions

import com.chummer.domain.category.Category
import com.chummer.domain.mapping.mono.toDbOperation
import com.chummer.finance.db.mono.transaction.UpsertTransactionsUseCase
import com.chummer.finance.network.monobank.transactions.GetTransactionsParameters
import com.chummer.finance.network.monobank.transactions.GetTransactionsUseCase
import com.chummer.finance.network.monobank.transactions.Transaction
import com.chummer.infrastructure.usecase.ExecutableUseCase
import com.chummer.models.mapping.toLocalDateTime
import kotlinx.coroutines.Dispatchers
import java.time.temporal.ChronoUnit
import kotlin.coroutines.CoroutineContext
import kotlin.math.absoluteValue

class FetchMonoTransactionsUseCase(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val upsertTransactionsUseCase: UpsertTransactionsUseCase
) : ExecutableUseCase<FetchTransactionsArgument, FetchTransactionsResult>(KEY) {
    override val coroutineContext: CoroutineContext = Dispatchers.Main

    override suspend fun execute(input: FetchTransactionsArgument): FetchTransactionsResult {
        val (accountId, jarId, from, to) = input
        val account = accountId ?: jarId ?: error(
            getArgumentsMessage("account")
        )

        val requestExceedsTimeLimit =
            ChronoUnit.DAYS.between(from, to).absoluteValue > REQUEST_DAYS_LIMIT

        val actualFrom = if (requestExceedsTimeLimit)
            to.minusDays(REQUEST_DAYS_LIMIT)
        else from

        val transactions = getTransactionsUseCase(
            GetTransactionsParameters(
                account = account,
                from = actualFrom,
                to = to
            )
        )
        upsertTransactionsUseCase(
            transactions.map {
                it.toDbOperation(
                    accountId = input.accountId,
                    jarId = jarId,
                    categoryId = it.getCategoryId()
                )
            }
        )
        return when {
            transactions.isNotEmpty() -> FetchTransactionsResult.TransactionsReturned(
                fetchFullyCompleted = transactions.size < MAX_TRANSACTIONS_IN_RESPONSE && !requestExceedsTimeLimit,
                recentTransactionDateTime = transactions.maxOf { it.time }.toLocalDateTime(),
                oldestTransactionDateTime = transactions.minOf { it.time }.toLocalDateTime()
            )

            else -> FetchTransactionsResult.NoTransactionsReturned(to)
        }
    }

    private fun getArgumentsMessage(argument: String) = "Missing \"$argument\" argument"

    private companion object {
        const val KEY = "FETCH_MONO_TRANSACTIONS"
        const val MAX_TRANSACTIONS_IN_RESPONSE = 500
        const val REQUEST_DAYS_LIMIT = 30L
    }
}

private fun Transaction.getCategoryId(): Int {
    val category = when {
        else -> Category.OTHER
    }

    return category.id
}
