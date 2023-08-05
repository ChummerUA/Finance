package com.chummer.domain.mapping.mono

import mono.Account as DbAccount
import com.chummer.networkmodels.mono.Account as NetworkAccount

fun NetworkAccount.toDbModel(clientId: String) = DbAccount(
    id = id,
    balance = balance,
    credit_limit = creditLimit,
    type = type.value,
    masked_pans = maskedPans.joinToString(SEPARATOR).takeIf { it.isNotBlank() },
    client_id = clientId
)

const val SEPARATOR = ","
