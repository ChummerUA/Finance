package com.chummer.finance.db.mono.transaction

import app.cash.sqldelight.TransactionWithoutReturn
import com.chummer.infrastructure.db.useCases.transaction.withoutResult.DbTransactionUseCase

class UpsertTransactionsUseCase(
    transacter: TransactionQueries
) : DbTransactionUseCase<List<Transaction>, TransactionQueries>(KEY, transacter) {
    override fun TransactionWithoutReturn.execute(argument: List<Transaction>) {
        argument.forEach { operation ->
            with(operation) {
                transacter.upsertOperation(
                    time = time,
                    description = description,
                    mcc = mcc,
                    originalMcc = original_mcc,
                    hold = hold,
                    amount = amount,
                    operationAmount = operation_amount,
                    currencyCode = currency_code,
                    commissionRate = commission_rate,
                    cashbackAmount = cashback_amount,
                    balance = balance,
                    comment = comment,
                    receiptId = receipt_id,
                    invoiceId = invoice_id,
                    counterEdrpou = counter_edrpou,
                    counterIban = counter_iban,
                    id = id,
                    accountId = account_id,
                    jarId = jar_id,
                    categoryId = categoryId
                )
            }
        }
    }

    private companion object {
        const val KEY = "upsert_transactions"
    }
}
