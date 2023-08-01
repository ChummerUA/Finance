package com.chummer.networkmodels.mono

import kotlinx.serialization.Serializable

@Serializable
data class GetTransactionsRequest(
    val account: String,
    val from: Long,
    val to: Long
)

data class GetTransactionsResponse(
    val id: String,
    val time: Long,
    val description: String,
    val mcc: Int,
    val originalMcc: Int,
    val hold: Boolean,
    val amount: Long,
    val operationAmount: Long,
    val currencyCode: Int,
    val commissionRate: Long,
    val cashbackAmount: Long,
    val balance: Long,
    val comment: String?,
    val receiptId: String?,
    val invoiceId: String?,
    val counterEdrpou: String?,
    val counterIban: String?,
    val counterName: String
)