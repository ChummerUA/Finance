package com.chummer.domain.mono.transactions

import com.chummer.domain.mapping.mono.toDbOperation
import com.chummer.finance.db.mono.transaction.UpsertTransactionsUseCase
import com.chummer.finance.network.monobank.transactions.GetTransactionsUseCase
import com.chummer.infrastructure.usecase.ExecutableUseCase
import com.chummer.models.None
import com.chummer.models.mono.GetTransactionsParameters
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class FetchTransactionsUseCase(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val upsertTransactionsUseCase: UpsertTransactionsUseCase
) : ExecutableUseCase<FetchTransactionsArgument, None>(KEY) {
    override val coroutineContext: CoroutineContext = Dispatchers.Main

    override suspend fun execute(input: FetchTransactionsArgument) {
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
    }

    private fun getArgumentsMessage(argument: String) = "Missing \"$argument\" argument"

    private companion object {
        private const val KEY = "FETCH_OPERATIONS"
    }
}
