package com.chummer.domain.mono

import com.chummer.domain.mapping.mono.toDbOperation
import com.chummer.finance.ChummerFinanceDatabase
import com.chummer.finance.db.mono.operation.UpsertOperationsUseCase
import com.chummer.finance.network.monobank.transactions.GetTransactionsUseCase
import com.chummer.infrastructure.usecase.ExecutableUseCase
import com.chummer.models.None
import com.chummer.models.mono.GetTransactionsParameters
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class FetchOperationsUseCase(
    client: HttpClient,
    db: ChummerFinanceDatabase
): ExecutableUseCase<GetTransactionsParameters, None>(KEY) {
    private val getTransactionsUseCase = GetTransactionsUseCase(client)
    private val upsertOperationsUseCase = UpsertOperationsUseCase(db.operationQueries)

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    override suspend fun execute(input: GetTransactionsParameters) {
        val transactions = getTransactionsUseCase(input)
        upsertOperationsUseCase(
            transactions.map { it.toDbOperation() }
        )
    }
}

private const val KEY = "fetch_operations"
