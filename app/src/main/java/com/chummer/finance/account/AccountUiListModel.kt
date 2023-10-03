package com.chummer.finance.account

sealed class AccountUiListModel(
    open val id: Long,
    open val name: String,
    open val balance: String
) {
    data class Card(
        override val id: Long,
        override val name: String,
        override val balance: String,
        val creditMoneyTitle: String,
        val creditMoney: String
    ) : AccountUiListModel(id, name, balance)

    data class FOP(
        override val id: Long,
        override val name: String,
        override val balance: String
    ) : AccountUiListModel(id, name, balance)

    data class Jar(
        override val id: Long,
        override val name: String,
        override val balance: String,
        val targetTitle: String,
        val target: String
    ) : AccountUiListModel(id, name, balance)
}
