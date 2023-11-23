package com.chummer.finance.ui.account

import android.content.Context
import com.chummer.finance.R
import com.chummer.finance.db.mono.account.AccountItem
import com.chummer.finance.ui.transaction.TransactionUiListModel
import com.chummer.finance.utils.getAccountName
import com.chummer.finance.utils.getFormattedAmountAndCurrency
import com.chummer.networkmodels.mono.CARD_TYPE_FOP
import kotlinx.collections.immutable.ImmutableList

sealed class AccountUiModel(
    open val id: String,
    open val title: String,
    open val selectAllText: String,
    open val balance: AccountInfoItem
) {
    data class Card(
        override val id: String,
        override val title: String,
        override val selectAllText: String,
        val cardNumber: AccountInfoItem,
        override val balance: AccountInfoItem,
        val creditInfo: AccountInfoItem?
    ) : AccountUiModel(
        id = id,
        title = title,
        selectAllText = selectAllText,
        balance = balance
    )

    data class FOP(
        override val id: String,
        override val title: String,
        override val selectAllText: String,
        val iban: AccountInfoItem,
        override val balance: AccountInfoItem,
        val creditInfo: AccountInfoItem?
    ) : AccountUiModel(
        id = id,
        title = title,
        selectAllText = selectAllText,
        balance = balance
    )
}

data class AccountInfoItem(
    val title: String,
    val value: String
)

data class DayWithTransactions(
    val day: String,
    val transactions: ImmutableList<TransactionUiListModel>
)

fun AccountItem.toUiModel(context: Context) = when (type) {
    CARD_TYPE_FOP -> toFopUiModel(context)
    else -> toCardUiModel(context)
}

private fun AccountItem.toCardUiModel(context: Context) = AccountUiModel.Card(
    id = id,
    title = getAccountName(currencyCode, type, context),
    selectAllText = context.getString(R.string.account_select_all_accounts),
    cardNumber = AccountInfoItem(
        context.getString(R.string.account_card_number),
        cardNumber
    ),
    balance = AccountInfoItem(
        context.getString(R.string.account_balance),
        getFormattedAmountAndCurrency(balance, currencyCode)
    ),
    creditInfo = getCreditInfo(context)
)

private fun AccountItem.toFopUiModel(context: Context) = AccountUiModel.FOP(
    id = id,
    title = getAccountName(currencyCode, type, context),
    selectAllText = context.getString(R.string.account_select_all_accounts),
    iban = AccountInfoItem(
        context.getString(R.string.account_iban),
        iban
    ),
    balance = AccountInfoItem(
        context.getString(R.string.account_balance),
        getFormattedAmountAndCurrency(balance, currencyCode)
    ),
    creditInfo = getCreditInfo(context)
)

private fun AccountItem.getCreditInfo(context: Context): AccountInfoItem? {
    return if (balance > creditLimit && creditLimit != 0L) AccountInfoItem(
        context.getString(R.string.account_your_money),
        getFormattedAmountAndCurrency(balance - creditLimit, currencyCode)
    ) else null
}
