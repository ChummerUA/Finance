package com.chummer.finance.db.mono.transaction.getTransactions

import android.util.Log
import app.cash.sqldelight.Query
import com.chummer.finance.db.mono.transaction.TransactionQueries
import com.chummer.finance.db.mono.transaction.getTransaction.GetTransactionsArgument
import com.chummer.infrastructure.db.useCases.flow.DbListFlowUseCase
import com.chummer.models.mapping.toUnixSecond
import java.time.LocalDateTime

@Suppress("LocalVariableName")
class GetTransactionsFlowUseCase(
    transacter: TransactionQueries
) : DbListFlowUseCase<GetTransactionsArgument, ListTransactionItem, TransactionQueries>(
    KEY,
    transacter
) {
    override fun TransactionQueries.getQuery(argument: GetTransactionsArgument): Query<ListTransactionItem> {
        val (accountId, pagingConfig, search, range, categoryIds) = argument
        val (time, pageSize, pages, shiftBack) = pagingConfig

        val forwardSize = if (shiftBack) (pages - 1) * pageSize else pages * pageSize
        val backwardSize = if (shiftBack) pageSize else 0L

        val from = range?.first?.toUnixSecond() ?: 0L
        val to = range?.second?.plusDays(1L)?.toUnixSecond() ?: LocalDateTime.now().toUnixSecond()

        Log.d(
            KEY,
            "Getting operations. Time: $time, forwardSize: $forwardSize, backwardSize: $backwardSize"
        )
        return transacter.getOperations(
            time = time,
            accountId = accountId,
            descriptionFilter = search,
            from = from,
            to = to,
            forwardSize = forwardSize,
            backwardSize = backwardSize,
            categoryIds = categoryIds
        ) { id, transactionTime, description, operation_amount, currency_code, mcc, original_mcc, cashback_amount, categoryId ->
            ListTransactionItem(
                id = id,
                time = transactionTime,
                description = description,
                operationAmount = operation_amount,
                currencyCode = currency_code,
                mcc = mcc,
                originalMcc = original_mcc,
                cashback = cashback_amount,
                categoryId = categoryId
            )
        }
    }

    private companion object {
        const val KEY = "get_operations"
    }
}
