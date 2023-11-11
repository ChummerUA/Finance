package com.chummer.finance.db.mono.operation.getOperations

import app.cash.sqldelight.Query
import com.chummer.finance.db.mono.operation.getOperation.GetOperationArgument
import com.chummer.infrastructure.db.useCases.flow.DbListFlowUseCase
import mono.OperationQueries

@Suppress("LocalVariableName")
class GetOperationsUseCase(
    transacter: OperationQueries
) : DbListFlowUseCase<GetOperationArgument, ListOperationItem, OperationQueries>(KEY, transacter) {
    override fun OperationQueries.getQuery(argument: GetOperationArgument): Query<ListOperationItem> {
        val (accountId, jarId, from, pageSize) = argument
        return transacter.getOperations(
            time = from,
            pageSize = pageSize.toLong(),
            accountId = accountId,
            jarId = jarId
        ) { id, time, description, operation_amount, currency_code, mcc, original_mcc, cashback_amount ->
            ListOperationItem(
                id = id,
                time = time,
                description = description,
                operationAmount = operation_amount,
                currencyCode = currency_code,
                mcc = mcc,
                originalMcc = original_mcc,
                cashback = cashback_amount
            )
        }
    }
}

private const val KEY = "get_operations"
