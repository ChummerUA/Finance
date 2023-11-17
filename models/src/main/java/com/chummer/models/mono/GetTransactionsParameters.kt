package com.chummer.models.mono

import java.time.LocalDateTime

data class GetTransactionsParameters(
    val account: String,
    val from: LocalDateTime,
    val to: LocalDateTime
)
