package com.chummer.domain.mapping.mono

import com.chummer.finance.network.monobank.account.Jar as NetworkJar
import mono.Jar as DbJar

fun NetworkJar.toDbModel(clientId: String) = DbJar(
    id = id,
    title = title,
    description = description,
    currency_code = currencyCode,
    balance = balance,
    goal = goal,
    client_id = clientId,
    is_deleted = false
)
