package com.chummer.finance.db.mono.account.getAccountFlow

import app.cash.sqldelight.Query
import com.chummer.finance.db.mono.account.AccountItem
import com.chummer.finance.db.mono.account.CashbackType
import com.chummer.infrastructure.db.useCases.flow.DbItemFlowUseCase
import mono.AccountQueries

class GetAccountFlowUseCase(
    queries: AccountQueries
) : DbItemFlowUseCase<String, AccountItem, AccountQueries>(KEY, queries) {
    override fun AccountQueries.getQuery(accountId: String): Query<AccountItem> {
        return getAccountById(accountId) { id, balance, creditLimit, type, cashbackType, currencyCode, iban, maskedPans, _, _ ->
            AccountItem(
                id = id,
                balance = balance,
                creditLimit = creditLimit,
                currencyCode = currencyCode,
                cashbackType = CashbackType.fromString(cashbackType),
                iban = iban,
                type = type,
                cardNumber = maskedPans
                    .split(",")
                    .map { it.trim() }
                    .firstOrNull() ?: CARD_PLACEHOLDER
            )
        }
    }

    companion object {
        private const val KEY = "GET_ACCOUNT_FLOW"
        private const val CARD_PLACEHOLDER = "XXXX XXXX XXXX XXXX"
    }
}
