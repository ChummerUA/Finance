package com.chummer.finance.db.mono.account

data class AccountListItem(
    val id: String,
    val name: String,
    val balance: Long,
    val creditLimit: Long,
    val currencyCode: Int,
    val type: String
)
