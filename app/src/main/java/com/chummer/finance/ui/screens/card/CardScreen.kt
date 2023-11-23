package com.chummer.finance.ui.screens.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chummer.finance.navigation.nodes.AccountNode.SelectAccount
import com.chummer.finance.navigation.popUpTo
import com.chummer.finance.ui.BalanceView
import com.chummer.finance.ui.DividerView
import com.chummer.finance.ui.account.AccountUiModel
import com.chummer.finance.ui.account.AccountUiModel.Card
import com.chummer.finance.ui.account.AccountUiModel.FOP
import com.chummer.finance.ui.account.Display
import com.chummer.finance.ui.text.ClickableText
import com.chummer.finance.ui.text.ItemTitleText
import com.chummer.finance.ui.theme.AppTheme
import com.chummer.finance.ui.transaction.transactions
import com.chummer.finance.utils.rememberStateWithLifecycle

@Composable
fun CardScreen(
    navController: NavController,
    screenViewModel: CardViewModel = hiltViewModel()
) {
    val state by rememberStateWithLifecycle(screenViewModel.state)

    val onSelectAccountClicked = remember {
        { navController.popUpTo(SelectAccount.fullRoute) }
    }
    state?.DisplayContent(onSelectAccountClicked)
}

@Composable
fun CardUiState.DisplayContent(
    onSelectAccountClicked: (() -> Unit)
) = LazyColumn {
    item(key = "account") {
        when (account) {
            is Card -> account.Display(onSelectAccountClicked)
            is FOP -> account.Display(onSelectAccountClicked)
        }
    }
    transactions(daysWithTransactions)
}

@Composable
private fun Card.Display(
    onSelectAccountClicked: (() -> Unit)
) {
    CardNameView(onSelectAccountClicked)

    cardNumber.Display()

    val dividerPadding = remember { PaddingValues(horizontal = 16.dp) }
    DividerView(
        paddingValues = dividerPadding
    )

    BalanceView(balance, creditInfo)

    DividerView(
        paddingValues = dividerPadding
    )
}

@Composable
private fun FOP.Display(
    onSelectAccountClicked: (() -> Unit)
) {
    CardNameView(onSelectAccountClicked)

    cardNumber.Display()
    iban.Display()

    val dividerPadding = remember { PaddingValues(horizontal = 16.dp) }
    DividerView(
        paddingValues = dividerPadding
    )

    BalanceView(balance, creditInfo)

    DividerView(
        paddingValues = dividerPadding
    )
}

@Composable
private fun AccountUiModel.CardNameView(
    onSelectAccountClicked: () -> Unit
) = Row(
    Modifier
        .clickable(onClick = onSelectAccountClicked)
        .padding(horizontal = 16.dp, vertical = 8.dp)
) {
    val colors = AppTheme.colors
    ItemTitleText(
        text = title,
        color = colors.textPrimary,
        modifier = Modifier
            .weight(1f)
            .wrapContentHeight()
    )
    ClickableText(
        text = selectAllText,
        color = colors.primary
    )
}
