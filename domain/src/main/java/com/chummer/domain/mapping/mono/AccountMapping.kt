package com.chummer.domain.mapping.mono

import com.chummer.finance.network.monobank.account.Account as NetworkAccount
import mono.Account as DbAccount

fun NetworkAccount.toDbModel(clientId: String) = DbAccount(
    id = id,
    balance = balance,
    credit_limit = creditLimit,
    type = type,
    cashback_type = cashbackType,
    masked_pans = maskedPans.joinToString(SEPARATOR),
    currency_code = currencyCode,
    client_id = clientId,
    iban = iban,
    is_deleted = false
)

const val SEPARATOR = ","
