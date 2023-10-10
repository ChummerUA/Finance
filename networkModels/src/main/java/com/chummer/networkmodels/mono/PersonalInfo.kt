package com.chummer.networkmodels.mono

import kotlinx.serialization.SerialName
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
    val type: CardType,
    val currencyCode: Int,
    @SerialName("maskedPan")
    val maskedPans: List<String>
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
enum class CardType(
    val value: Int
) {
    @SerialName("black")
    BLACK(0),

    @SerialName("white")
    WHITE(1),

    @SerialName("platinum")
    PLATINUM(2),

    @SerialName("iron")
    IRON(3),

    @SerialName("fop")
    FOP(4),

    @SerialName("yellow")
    YELLOW(5),

    @SerialName("e_aid")
    EAID(6)
}
