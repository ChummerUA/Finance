package com.chummer.finance.db.mono.account

data class AccountListItem(
    val id: String,
    val name: String,
    val balance: Long,
    val creditLimit: Long,
    val currencyCode: Int,
    val type: AccountType
)

enum class AccountType(
    val value: Int
) {
    BLACK(0),
    WHITE(1),
    PLATINUM(2),
    IRON(3),
    FOP(4),
    YELLOW(5),
    EAID(6)
}
