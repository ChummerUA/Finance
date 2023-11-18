package com.chummer.finance.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chummer.finance.ui.account.AccountInfoItem
import com.chummer.finance.ui.text.GroupTitleText
import com.chummer.finance.ui.text.ItemDescriptionText
import com.chummer.finance.ui.text.ItemTitleText
import com.chummer.finance.ui.text.LargePriceText
import com.chummer.finance.ui.theme.AppTheme

@Composable
fun BalanceView(
    item: AccountInfoItem,
    creditInfo: AccountInfoItem?
) = Column(
    Modifier.padding(
        horizontal = 16.dp,
        vertical = 12.dp
    )
) {
    val colors = AppTheme.colors
    GroupTitleText(
        text = item.title,
        color = colors.textSecondary
    )

    if (creditInfo != null) {
        Column(
            Modifier.padding(
                vertical = 8.dp
            )
        ) {
            ItemDescriptionText(
                text = creditInfo.title,
                color = colors.textSecondary
            )
            ItemTitleText(
                text = creditInfo.value,
                color = colors.textPrimary
            )
        }
    }
    LargePriceText(text = item.value)
}
