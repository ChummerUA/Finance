package com.chummer.finance.db.mono.transaction.getTransactions

import app.cash.sqldelight.Query
import com.chummer.finance.db.mono.transaction.TransactionQueries
import com.chummer.finance.db.mono.transaction.getTransaction.GetTransactionsArgument
import com.chummer.infrastructure.db.useCases.flow.DbListFlowUseCase

@Suppress("LocalVariableName")
class GetTransactionsFlowUseCase(
    transacter: TransactionQueries
) : DbListFlowUseCase<GetTransactionsArgument, ListTransactionItem, TransactionQueries>(
    KEY,
    transacter
) {
    override fun TransactionQueries.getQuery(argument: GetTransactionsArgument): Query<ListTransactionItem> {
        val (accountId, jarId, from, pageSize) = argument
        return transacter.getOperations(
            time = from,
            pageSize = pageSize.toLong(),
            accountId = accountId,
            jarId = jarId
        ) { id, time, description, operation_amount, currency_code, mcc, original_mcc, cashback_amount ->
            ListTransactionItem(
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

    private companion object {
        const val KEY = "get_operations"
    }
}
