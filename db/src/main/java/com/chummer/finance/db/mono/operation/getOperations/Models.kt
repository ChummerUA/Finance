package com.chummer.finance.db.mono.operation.getOperations

data class ListOperationItem(
    val id: String,
    val time: Long,
    val description: String,
    val operationAmount: Long,
    val currencyCode: Long,
    val mcc: Int,
    val originalMcc: Int,
    val cashback: Long
)
