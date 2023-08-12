package com.chummer.domain.mono

import com.chummer.domain.mapping.mono.toDbOperation
import com.chummer.finance.db.mono.UpsertOperationsUseCase
import com.chummer.finance.network.monobank.transactions.GetTransactionsUseCase
import com.chummer.infrastructure.usecase.ExecutableUseCase
import com.chummer.models.None
import com.chummer.models.mono.GetTransactionsParameters
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class FetchOperationsUseCase(
    val getTransactionsUseCase: GetTransactionsUseCase,
    val upsertOperationsUseCase: UpsertOperationsUseCase
): ExecutableUseCase<GetTransactionsParameters, None>(KEY) {
    override val coroutineContext: CoroutineContext = Dispatchers.Main

    override suspend fun invoke(input: GetTransactionsParameters) {
        val transactions = getTransactionsUseCase(input)
        upsertOperationsUseCase(
            transactions.map { it.toDbOperation() }
        )
    }
}

private const val KEY = "fetch_operations"
