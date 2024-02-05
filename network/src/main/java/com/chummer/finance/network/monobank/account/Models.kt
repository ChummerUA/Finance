package com.chummer.finance.network.monobank.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountResponse(
    val clientId: String,
    val name: String,
    val accounts: List<Account>? = null,
    val jars: List<Jar>? = null
)

@Serializable
data class Account(
    val id: String,
    val balance: Long,
    val creditLimit: Long,
    val cashbackType: String? = null,
    val type: String,
    val currencyCode: Int,
    val iban: String,
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

const val CARD_TYPE_BLACK = "black"
const val CARD_TYPE_WHITE = "white"
const val CARD_TYPE_PLATINUM = "platinum"
const val CARD_TYPE_IRON = "iron"
const val CARD_TYPE_FOP = "fop"
const val CARD_TYPE_YELLOW = "yellow"
const val CARD_TYPE_EAID = "eAid"
