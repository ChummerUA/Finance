package com.chummer.finance.transaction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chummer.finance.text.ItemDescriptionText
import com.chummer.finance.text.ItemTitleText
import com.chummer.finance.theme.AppTheme

@Composable
fun TransactionListItemView(
    transaction: TransactionUiListModel
) = Row {
    val colors = AppTheme.colors
    TransactionIconView(icon = transaction.icon)
    Spacer(modifier = Modifier.width(12.dp))
    Column {
        ItemTitleText(text = transaction.name, color = colors.textPrimary)
        ItemDescriptionText(text = transaction.time, color = colors.textSecondary)
    }

    val amountTextColor by remember(transaction.income) {
        derivedStateOf {
            if (transaction.income)
                colors.income
            else
                colors.textPrimary
        }
    }
    ItemTitleText(text = transaction.price, amountTextColor)
}

@Composable
fun TransactionIconView(icon: TransactionIconUiModel) {

}
