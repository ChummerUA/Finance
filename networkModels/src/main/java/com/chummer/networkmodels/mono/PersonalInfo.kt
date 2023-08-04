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
enum class CardType {
    @SerialName("black")
    BLACK,

    @SerialName("white")
    WHITE,

    @SerialName("platinum")
    PLATINUM,

    @SerialName("iron")
    IRON,

    @SerialName("fop")
    FOP,

    @SerialName("yellow")
    YELLOW,

    @SerialName("e_aid")
    E_AID
}
