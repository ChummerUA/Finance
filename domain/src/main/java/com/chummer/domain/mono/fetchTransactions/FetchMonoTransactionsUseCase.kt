package com.chummer.domain.mono.fetchTransactions

import com.chummer.domain.mapping.mono.toDbOperation
import com.chummer.finance.db.mono.transaction.UpsertTransactionsUseCase
import com.chummer.finance.network.monobank.transactions.GetTransactionsUseCase
import com.chummer.infrastructure.usecase.ExecutableUseCase
import com.chummer.models.mapping.toLocalDateTime
import com.chummer.models.mono.GetTransactionsParameters
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

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
        assert(from > 0) { getArgumentsMessage("from") }
        assert(to > 0) { getArgumentsMessage("to") }

        val transactions = getTransactionsUseCase(
            GetTransactionsParameters(
                account = account,
                from = from,
                to = to
            )
        )
        upsertTransactionsUseCase(
            transactions.map {
                it.toDbOperation(
                    accountId = input.accountId,
                    jarId = jarId
                )
            }
        )
        return when {
            transactions.isNotEmpty() -> FetchTransactionsResult.TransactionsReturned(
                fetchFullyCompleted = transactions.size == MAX_TRANSACTIONS_IN_RESPONSE,
                recentTransactionDateTime = transactions.maxOf { it.time }.toLocalDateTime(),
                oldestTransactionDateTime = transactions.maxOf { it.time }.toLocalDateTime()
            )

            else -> FetchTransactionsResult.NoTransactionsReturned
        }
    }

    private fun getArgumentsMessage(argument: String) = "Missing \"$argument\" argument"

    private companion object {
        const val KEY = "FETCH_MONO_TRANSACTIONS"
        const val MAX_TRANSACTIONS_IN_RESPONSE = 500
    }
}
