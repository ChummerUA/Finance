package com.chummer.finance.ui.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.chummer.finance.ui.spacing.Fill
import com.chummer.finance.ui.text.GroupTitleText
import com.chummer.finance.ui.text.ItemDescriptionText
import com.chummer.finance.ui.text.ItemTitleText
import com.chummer.finance.ui.text.MediumPriceText
import com.chummer.finance.ui.theme.AppTheme
import com.chummer.finance.utils.mergeContentDescription

@Composable
fun AccountListItem(
    item: AccountUiListModel,
    onItemClicked: () -> Unit
) {
    when (item) {
        is AccountUiListModel.Card -> Account(item, onItemClicked)
        is AccountUiListModel.FOP -> Fop(item, onItemClicked)
        is AccountUiListModel.Jar -> Jar(item, onItemClicked)
    }
}

@Composable
fun Account(
    card: AccountUiListModel.Card,
    onCardClicked: () -> Unit
) = with(card) {
    val additionalInfo = remember(id, creditMoney) {
        AccountCardViewAdditionalInfo(
            creditMoneyTitle,
            creditMoney
        )
    }
    AccountCard(
        name = name,
        balance = balance,
        additionalInfo = additionalInfo,
        onCardClicked = onCardClicked
    )
}

@Composable
fun Fop(
    fop: AccountUiListModel.FOP,
    onCardClicked: () -> Unit
) = with(fop) {
    AccountCard(
        name = name,
        balance = balance,
        additionalInfo = null,
        onCardClicked = onCardClicked
    )
}

@Composable
fun Jar(
    jar: AccountUiListModel.Jar,
    onCardClicked: () -> Unit
) = with(jar) {
    val additionalInfo = remember(id, goal) {
        AccountCardViewAdditionalInfo(
            goalTitle,
            goal
        )
    }
    AccountCard(
        name = name,
        balance = balance,
        additionalInfo = additionalInfo,
        onCardClicked = onCardClicked
    )
}

@Composable
private fun AccountCard(
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

private data class AccountCardViewAdditionalInfo(
    val title: String,
    val value: String
)

@Composable
fun AccountInfoItem(item: AccountInfoItem) = Column(
    Modifier
        .fillMaxWidth(1f)
        .mergeContentDescription()
        .padding(
            vertical = 12.dp,
            horizontal = 16.dp
        )
) {
    with(item) {
        GroupTitleText(text = title, color = AppTheme.colors.textSecondary)
        ItemTitleText(text = value, color = AppTheme.colors.textPrimary)
    }
}
