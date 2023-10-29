package com.chummer.domain.mapping.mono

import com.chummer.networkmodels.mono.Account as NetworkAccount
import mono.Account as DbAccount

fun NetworkAccount.toDbModel(clientId: String) = DbAccount(
    id = id,
    balance = balance,
    credit_limit = creditLimit,
    type = type,
    masked_pans = maskedPans.joinToString(SEPARATOR),
    currency_code = currencyCode,
    client_id = clientId
)

const val SEPARATOR = ","
