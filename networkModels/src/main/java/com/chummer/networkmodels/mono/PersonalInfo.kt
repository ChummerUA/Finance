package com.chummer.networkmodels.mono

import kotlinx.serialization.Serializable

@Serializable
data class AccountResponse(
    val clientId: String,
    val name: String,
    val accounts: List<Account>,
    val jars: List<Jar>
)

@Serializable
data class Account(
    val id: String,
    val balance: Long,
    val creditLimit: Long,
    val type: CardType
)

@Serializable
data class Jar(
    val id: String,
    val title: String,
    val description: String,
    val currencyCode: Int,
    val balance: Long,
    val goal: Long
)

@Serializable
enum class CardType {
    BLACK,
    WHITE,
    PLATINUM,
    IRON,
    FOP,
    YELLOW,
    E_AID
}
