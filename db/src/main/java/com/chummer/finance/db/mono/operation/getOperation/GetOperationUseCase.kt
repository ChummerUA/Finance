package com.chummer.finance.db.mono.operation.getOperation

import app.cash.sqldelight.Query
import com.chummer.infrastructure.db.useCases.flow.DbItemFlowUseCase
import kotlinx.coroutines.Dispatchers
import mono.Operation
import mono.OperationQueries
import kotlin.coroutines.CoroutineContext

class GetOperationUseCase(
    transacter: OperationQueries
) : DbItemFlowUseCase<String, Operation, OperationQueries>(KEY, transacter) {
    override val coroutineContext: CoroutineContext = Dispatchers.IO

    override fun OperationQueries.getQuery(argument: String): Query<Operation> {
        return transacter.getOpertionById(argument)
    }
}

private const val KEY = "get_operation"
