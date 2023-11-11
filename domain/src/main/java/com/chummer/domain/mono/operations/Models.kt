package com.chummer.domain.mono.operations

data class FetchOperationsInput(
    val accountId: String?,
    val jarId: String?,
    val from: Long,
    val to: Long
)
