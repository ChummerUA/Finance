package com.chummer.finance.ui.screens.account.selectAccount

import android.content.Context
import com.chummer.domain.ClientAccountListItem
import com.chummer.finance.R
import com.chummer.finance.db.mono.account.AccountListItem
import com.chummer.finance.db.mono.jar.JarListItem
import com.chummer.finance.utils.getAccountName
import com.chummer.finance.utils.getFormattedAmountAndCurrency
import com.chummer.networkmodels.mono.CARD_TYPE_FOP

sealed class AccountUiListModel(
    open val id: String,
    open val name: String,
    open val balance: String
) {
    data class Card(
        override val id: String,
        override val name: String,
        override val balance: String,
        val creditMoneyTitle: String,
        val creditMoney: String
    ) : AccountUiListModel(id, name, balance)

    data class FOP(
        override val id: String,
        override val name: String,
        override val balance: String
    ) : AccountUiListModel(id, name, balance)

    data class Jar(
        override val id: String,
        override val name: String,
        override val balance: String,
        val goalTitle: String,
        val goal: String
    ) : AccountUiListModel(id, name, balance)
}

fun ClientAccountListItem.toUiModel(
    context: Context
): AccountUiListModel = when(this) {
    is ClientAccountListItem.Account -> when(info.type) {
        CARD_TYPE_FOP -> info.toFopUiModel()
        else -> info.toCardUiModel(context)
    }
    is ClientAccountListItem.Jar -> info.toUiModel(context)
}

fun AccountListItem.toCardUiModel(context: Context) = AccountUiListModel.Card(
    id = id,
    name = getAccountName(currencyCode, type, context),
    balance = getFormattedAmountAndCurrency(balance, currencyCode),
    creditMoneyTitle = context.getString(R.string.account_your_money),
    creditMoney = getFormattedAmountAndCurrency(balance - creditLimit, currencyCode)
)

fun AccountListItem.toFopUiModel() = AccountUiListModel.FOP(
    id = id,
    name = getAccountName(currencyCode),
    balance = getFormattedAmountAndCurrency(balance, currencyCode)
)

fun JarListItem.toUiModel(context: Context) = AccountUiListModel.Jar(
    id = id,
    name = name,
    balance = getFormattedAmountAndCurrency(balance, currencyCode),
    goalTitle = context.getString(R.string.jar_goal),
    goal = getFormattedAmountAndCurrency(goal, currencyCode)
)
