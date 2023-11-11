package com.chummer.finance.ui.screens.transaction

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chummer.finance.ui.screens.account.DayWithTransactions
import com.chummer.finance.ui.spacing.Space
import com.chummer.finance.ui.text.ItemDescriptionText
import com.chummer.finance.ui.text.ItemTitleText
import com.chummer.finance.ui.theme.AppTheme
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.transactions(daysWithTransactions: ImmutableList<DayWithTransactions>) {
    for (day in daysWithTransactions) {
        stickyHeader {
            DayHeader(day.day)
        }
        items(day.transactions, key = { it.id }) {
            TransactionListItemView(transaction = it)
        }
    }
}

@Composable
private fun DayHeader(day: String) = Row(
    modifier = Modifier
        .fillMaxWidth(1f)
        .padding(vertical = 4.dp),
    horizontalArrangement = Arrangement.Center
) {
    ItemTitleText(
        text = day,
        color = AppTheme.colors.textPrimary,
        modifier = Modifier
            .background(
                color = AppTheme.colors.backgroundSecondary,
                shape = RoundedCornerShape(percent = 50)
            )
            .padding(
                horizontal = 12.dp,
                vertical = 8.dp
            )
    )
}

@Composable
fun TransactionListItemView(
    transaction: TransactionUiListModel
) = Row(
    modifier = Modifier.padding(
        horizontal = 16.dp,
        vertical = 8.dp
    ),
    verticalAlignment = Alignment.CenterVertically
) {
    val colors = AppTheme.colors
    TransactionIconView(icon = transaction.icon)

    Space(12.dp)

    Column(Modifier.weight(1f)) {
        ItemTitleText(text = transaction.name, color = colors.textPrimary)
        Space(4.dp)
        ItemDescriptionText(text = transaction.time, color = colors.textSecondary)
    }

    Space(12.dp)

    val amountTextColor by remember(transaction.income) {
        derivedStateOf {
            if (transaction.income)
                colors.income
            else
                colors.textPrimary
        }
    }
    ItemTitleText(text = transaction.amount, amountTextColor)
}

@Composable
fun TransactionIconView(icon: TransactionIconUiModel?) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(color = AppTheme.colors.income, shape = CircleShape)
    )
}
