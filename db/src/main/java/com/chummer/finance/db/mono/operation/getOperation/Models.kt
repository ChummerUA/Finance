package com.chummer.finance.db.mono.operation.getOperation

data class GetOperationArgument(
    val accountId: String?,
    val jarId: String?,
    val time: Long,
    val pageSize: Int
)
