package com.chummer.finance.db.mono.transaction.getTransaction

import app.cash.sqldelight.Query
import com.chummer.finance.db.mono.transaction.Transaction
import com.chummer.finance.db.mono.transaction.TransactionQueries
import com.chummer.infrastructure.db.useCases.flow.DbItemFlowUseCase
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class GetTransactionFlowUseCase(
    transacter: TransactionQueries
) : DbItemFlowUseCase<String, Transaction, TransactionQueries>(KEY, transacter) {
    override val coroutineContext: CoroutineContext = Dispatchers.IO

    override fun TransactionQueries.getQuery(argument: String): Query<Transaction> {
        return transacter.getOpertionById(argument)
    }

    private companion object {
        const val KEY = "get_operation"
    }
}
