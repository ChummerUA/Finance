package com.chummer.finance.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chummer.finance.ui.screens.account.AccountInfoItem
import com.chummer.finance.ui.text.GroupTitleText
import com.chummer.finance.ui.text.ItemTitleText
import com.chummer.finance.ui.theme.AppTheme

@Composable
fun AccountItemInfoView(
    info: AccountInfoItem
) = Column(
    Modifier.padding(
        vertical = 12.dp,
        horizontal = 16.dp
    )
) {
    GroupTitleText(text = info.title, color = AppTheme.colors.textSecondary)
    ItemTitleText(text = info.value, color = AppTheme.colors.textPrimary)
}
