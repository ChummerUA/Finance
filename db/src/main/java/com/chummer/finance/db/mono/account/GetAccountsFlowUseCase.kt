package com.chummer.finance.db.mono.account

import app.cash.sqldelight.Query
import com.chummer.infrastructure.db.useCases.flow.DbListFlowUseCase
import com.chummer.models.None
import mono.AccountQueries

class GetAccountsFlowUseCase(
    queries: AccountQueries
) : DbListFlowUseCase<None, AccountListItem, AccountQueries>(KEY, queries) {
    override fun AccountQueries.getQuery(argument: None): Query<AccountListItem> {
        return getAccounts { id, balance, creditLimit, currencyCode, type, maskedPans ->
            AccountListItem(
                id = id,
                name = maskedPans.split(",").map { it.trim() }.first(),
                balance = balance,
                creditLimit = creditLimit,
                currencyCode = currencyCode,
                type = type
            )
        }
    }
}

private const val KEY = "get_accounts"
