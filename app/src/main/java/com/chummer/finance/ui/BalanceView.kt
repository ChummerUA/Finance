package com.chummer.finance.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chummer.finance.R
import com.chummer.finance.ui.account.AccountInfoItem
import com.chummer.finance.ui.text.GroupTitleText
import com.chummer.finance.ui.text.ItemDescriptionText
import com.chummer.finance.ui.text.ItemTitleText
import com.chummer.finance.ui.text.LargePriceText
import com.chummer.finance.ui.theme.AppTheme
import com.chummer.finance.utils.TALKBACK_PAUSE_SYMBOL
import com.chummer.finance.utils.contentDescription
import com.chummer.finance.utils.noContentDescription

@Composable
fun BalanceView(
    item: AccountInfoItem,
    creditInfo: AccountInfoItem?
) {
    val total = stringResource(id = R.string.balance_total)
    val contentDescription by remember() {
        derivedStateOf {
            val title = "${item.title}$TALKBACK_PAUSE_SYMBOL"
            val creditText = creditInfo?.let {
                "${it.title}: ${it.value}$TALKBACK_PAUSE_SYMBOL"
            }
            val totalText = "$total ${item.value}"
            "$title $creditText $totalText"
        }
    }
    Column(
        Modifier
            .fillMaxWidth(1f)
            .contentDescription(contentDescription, mergeDescendants = true)
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            )
    ) {
        val colors = AppTheme.colors
        GroupTitleText(
            text = item.title,
            color = colors.textSecondary,
            modifier = Modifier.noContentDescription()
        )

        if (creditInfo != null) {
            Column(
                Modifier.padding(
                    vertical = 8.dp
                )
            ) {
                ItemDescriptionText(
                    text = creditInfo.title,
                    color = colors.textSecondary,
                    modifier = Modifier.noContentDescription()
                )
                ItemTitleText(
                    text = creditInfo.value,
                    color = colors.textPrimary,
                    modifier = Modifier.noContentDescription()
                )
            }
        }
        LargePriceText(
            text = item.value,
            modifier = Modifier.noContentDescription()
        )
    }
}
