package com.chummer.models.mono

data class GetTransactionsParameters(
    val account: String,
    val from: Long,
    val to: Long
)
