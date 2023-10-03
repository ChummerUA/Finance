package com.chummer.finance.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.chummer.finance.spacing.Fill
import com.chummer.finance.text.ItemDescriptionText
import com.chummer.finance.text.ItemTitleText
import com.chummer.finance.text.MediumPriceText

@Composable
fun CardListItem(
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
fun FopListItem(
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
fun JarListItem(
    card: AccountUiListModel.Jar,
    onCardClicked: (() -> Unit)
) {
    with(card) {
        val additionalInfo = remember(id, target) {
            AccountCardViewAdditionalInfo(
                targetTitle,
                target
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
            .clickable(onClick = onCardClicked)
            .background(Color(0xFFE1E1E1))
            .padding(12.dp)
    ) {
        ItemTitleText(
            text = name,
            color = Color(0xFF292020)
        )
        Fill()
        ItemDescriptionText(
            text = additionalInfo?.title ?: "",
            color = Color(0xFFBEBEBE)
        )
        ItemDescriptionText(
            text = additionalInfo?.value ?: "",
            color = Color(0xFF292020)
        )
        MediumPriceText(text = balance)
    }
}

data class AccountCardViewAdditionalInfo(
    val title: String,
    val value: String
)
