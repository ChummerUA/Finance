package com.chummer.finance.ui.transaction

import android.content.Context
import com.chummer.finance.R
import com.chummer.finance.db.mono.transaction.getTransactions.ListTransactionItem
import com.chummer.finance.utils.getFormattedAmountAndCurrency
import com.chummer.finance.utils.toDateTimeString
import com.chummer.models.mapping.toLocalDateTime

fun ListTransactionItem.getContentDescription(context: Context): String {
    val amount = getFormattedAmountAndCurrency(operationAmount, currencyCode)
    val mainPart = when {
        isIncome -> context.getString(R.string.transaction_content_description_incoming)
        else -> context.getString(
            R.string.transaction_content_description_spent, amount, description
        )
    }

    val dateTime = this.time.toLocalDateTime().toDateTimeString(context)
    return "$mainPart $dateTime"
}
