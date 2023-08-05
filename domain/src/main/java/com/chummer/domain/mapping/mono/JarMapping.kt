package com.chummer.domain.mapping.mono

import com.chummer.networkmodels.mono.Jar as NetworkJar
import mono.Jar as DbJar

fun NetworkJar.toDbModel(clientId: String) = DbJar(
    id = id,
    title = title,
    description = description,
    currency_code = currencyCode,
    balance = balance,
    goal = goal,
    client_id = clientId
)
