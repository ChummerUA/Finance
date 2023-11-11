package com.chummer.finance.db.mono.account

data class AccountListItem(
    val id: String,
    val name: String,
    val balance: Long,
    val creditLimit: Long,
    val currencyCode: Int,
    val type: String
)

data class AccountItem(
    val id: String,
    val iban: String,
    val balance: Long,
    val creditLimit: Long,
    val currencyCode: Int,
    val type: String,
    val cashbackType: CashbackType,
    val cardNumber: String
)

enum class CashbackType {
    UNKNOWN,
    NONE,
    UAH,
    MILES;

    companion object {
        fun fromString(value: String?): CashbackType {
            return when (value) {
                null, "None" -> NONE
                "UAH" -> UAH
                "Miles" -> MILES
                else -> UNKNOWN
            }
        }
    }
}
