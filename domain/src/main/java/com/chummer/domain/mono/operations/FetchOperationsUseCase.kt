package com.chummer.domain.mono.operations

import com.chummer.domain.mapping.mono.toDbOperation
import com.chummer.finance.db.mono.operation.UpsertOperationsUseCase
import com.chummer.finance.network.monobank.transactions.GetTransactionsUseCase
import com.chummer.infrastructure.usecase.ExecutableUseCase
import com.chummer.models.None
import com.chummer.models.mono.GetTransactionsParameters
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class FetchOperationsUseCase(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val upsertOperationsUseCase: UpsertOperationsUseCase
) : ExecutableUseCase<FetchOperationsInput, None>(KEY) {
    override val coroutineContext: CoroutineContext = Dispatchers.Main

    override suspend fun execute(input: FetchOperationsInput) {
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
        upsertOperationsUseCase(
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
