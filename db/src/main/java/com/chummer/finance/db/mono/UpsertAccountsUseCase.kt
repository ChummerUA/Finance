package com.chummer.finance.db.mono

import app.cash.sqldelight.TransactionWithoutReturn
import com.chummer.infrastructure.db.useCases.transaction.withoutResult.DbTransactionUseCase
import mono.Account
import mono.AccountQueries

class UpsertAccountsUseCase(
    queries: AccountQueries
): DbTransactionUseCase<List<Account>, AccountQueries>(KEY, queries) {
    override fun TransactionWithoutReturn.execute(argument: List<Account>) {
        argument.forEach {
            transacter.upsertAccount(
                id = it.id,
                type = it.type,
                balance = it.balance,
                creditLimit = it.credit_limit,
                maskedPans = it.masked_pans
            )
        }
    }
}

private const val KEY = "upsert_accounts"
