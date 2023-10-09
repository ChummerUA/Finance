package com.chummer.finance.db.mono.operation.getOperations

import app.cash.sqldelight.Query
import com.chummer.finance.db.mono.operation.getOperation.GetOperationArgument
import com.chummer.infrastructure.db.useCases.flow.DbListFlowUseCase
import kotlinx.coroutines.Dispatchers
import mono.OperationQueries
import kotlin.coroutines.CoroutineContext

@Suppress("LocalVariableName")
class GetOperationsUseCase(
    transacter: OperationQueries
) : DbListFlowUseCase<GetOperationArgument, ListOperationItem, OperationQueries>(KEY, transacter) {
    override val coroutineContext: CoroutineContext = Dispatchers.IO

    override fun OperationQueries.getQuery(argument: GetOperationArgument): Query<ListOperationItem> {
        return transacter.getOperations(
            argument.time,
            argument.pageSize.toLong()
        ) { id, time, description, operation_amount, currency_code, mcc, original_mcc, cashback_amount ->
            ListOperationItem(
                id = id,
                time = time,
                description = description,
                operationAmount = operation_amount,
                currencyCode = currency_code.toLong(),
                mcc = mcc,
                originalMcc = original_mcc,
                cashback = cashback_amount
            )
        }
    }
}

private const val KEY = "get_operations"
