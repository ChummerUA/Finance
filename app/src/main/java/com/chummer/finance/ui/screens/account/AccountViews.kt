package com.chummer.finance.ui.screens.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.chummer.finance.ui.screens.account.selectAccount.AccountUiListModel
import com.chummer.finance.ui.spacing.Fill
import com.chummer.finance.ui.text.ItemDescriptionText
import com.chummer.finance.ui.text.ItemTitleText
import com.chummer.finance.ui.text.MediumPriceText
import com.chummer.finance.ui.theme.AppTheme

@Composable
fun CardListItemView(
    card: AccountUiListModel.Card,
    onCardClicked: (() -> Unit)
) {
    with(card) {
        val additionalInfo = remember(id, creditMoney) {
            AccountCardViewAdditionalInfo(
                creditMoneyTitle,
                creditMoney
            )
        }
        AccountCardView(
            name = name,
            balance = balance,
            additionalInfo = additionalInfo,
            onCardClicked = onCardClicked
        )
    }
}

@Composable
fun FopListItemView(
    fop: AccountUiListModel.FOP,
    onCardClicked: (() -> Unit)
) {
    with(fop) {
        AccountCardView(
            name = name,
            balance = balance,
            additionalInfo = null,
            onCardClicked = onCardClicked
        )
    }
}

@Composable
fun JarListItemView(
    card: AccountUiListModel.Jar,
    onCardClicked: (() -> Unit)
) {
    with(card) {
        val additionalInfo = remember(id, goal) {
            AccountCardViewAdditionalInfo(
                goalTitle,
                goal
            )
        }
        AccountCardView(
            name = name,
            balance = balance,
            additionalInfo = additionalInfo,
            onCardClicked = onCardClicked
        )
    }
}

@Composable
fun AccountCardView(
    name: String,
    balance: String,
    additionalInfo: AccountCardViewAdditionalInfo?,
    onCardClicked: () -> Unit
) {
    Column(
        Modifier
            .height(150.dp)
            .clip(shape = RoundedCornerShape(12.dp))
            .clickable(onClick = onCardClicked)
            .background(AppTheme.colors.backgroundSecondary)
            .padding(12.dp)
    ) {
        ItemTitleText(
            text = name,
            color = AppTheme.colors.textPrimary
        )
        Fill()
        ItemDescriptionText(
            text = additionalInfo?.title ?: "",
            color = AppTheme.colors.textSecondary
        )
        ItemDescriptionText(
            text = additionalInfo?.value ?: "",
            color = AppTheme.colors.textPrimary
        )
        MediumPriceText(text = balance)
    }
}

data class AccountCardViewAdditionalInfo(
    val title: String,
    val value: String
)
