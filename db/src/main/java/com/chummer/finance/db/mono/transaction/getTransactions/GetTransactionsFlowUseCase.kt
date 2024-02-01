package com.chummer.finance.db.mono.transaction.getTransactions

import android.util.Log
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
        val (accountId, from, pageSize, pages, shiftBack) = argument
        val forwardSize = if (shiftBack) (pages - 1) * pageSize else pages * pageSize
        val backwardSize = if (shiftBack) pageSize else 0L

        Log.d(
            KEY,
            "Getting operations. Time: $from, forwardSize: $forwardSize, backwardSize: $backwardSize"
        )
        return transacter.getOperations(
            time = from,
            accountId = accountId,
            forwardSize = forwardSize,
            backwardSize = backwardSize
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
