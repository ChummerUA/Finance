package com.chummer.finance.db.mono.jar

data class JarListItem(
    val id: String,
    val name: String,
    val balance: Long,
    val goal: Long,
    val currencyCode: Int
)
