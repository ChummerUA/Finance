package com.chummer.finance.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chummer.finance.ui.screens.account.AccountInfoItem
import com.chummer.finance.ui.text.GroupTitleText
import com.chummer.finance.ui.text.LargePriceText
import com.chummer.finance.ui.theme.AppTheme

@Composable
fun BalanceView(item: AccountInfoItem) = Column(
    Modifier.padding(
        horizontal = 16.dp,
        vertical = 12.dp
    )
) {
    GroupTitleText(text = item.title, color = AppTheme.colors.textSecondary)
    LargePriceText(text = item.value)
}
