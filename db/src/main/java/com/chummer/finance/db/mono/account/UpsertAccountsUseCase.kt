package com.chummer.finance.db.mono.account

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
                currencyCode = it.currency_code,
                creditLimit = it.credit_limit,
                maskedPans = it.masked_pans,
                clientId = it.client_id
            )
        }
    }
}

private const val KEY = "upsert_accounts"
