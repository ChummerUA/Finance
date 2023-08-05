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
    val value: String
) {
    @SerialName("black")
    BLACK("black"),

    @SerialName("white")
    WHITE("white"),

    @SerialName("platinum")
    PLATINUM("platinum"),

    @SerialName("iron")
    IRON("iron"),

    @SerialName("fop")
    FOP("fop"),

    @SerialName("yellow")
    YELLOW("yellow"),

    @SerialName("e_aid")
    EAID("e_aid")
}
