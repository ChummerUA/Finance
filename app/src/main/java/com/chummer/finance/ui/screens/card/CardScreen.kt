package com.chummer.finance.ui.screens.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chummer.finance.navigation.nodes.AccountNode
import com.chummer.finance.navigation.nodes.AccountNode.SelectAccount
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
import com.chummer.finance.utils.PagingDirection
import com.chummer.finance.utils.isScrollingUp
import com.chummer.finance.utils.itemsAfterStart
import com.chummer.finance.utils.itemsBeforeEnd
import com.chummer.finance.utils.rememberStateWithLifecycle

@Composable
fun CardScreen(
    navController: NavController,
    screenViewModel: CardViewModel = hiltViewModel()
) {
    val state by rememberStateWithLifecycle(screenViewModel.state)

    val onSelectAccountClicked: ((String) -> Unit) = remember {
        { id ->
            // TODO figure out why card screen not popped up
            navController.navigate(SelectAccount.fullRoute) {
                popUpTo(AccountNode.Card.resolve(id)) {
                    inclusive = true
                }
            }
        }
    }

    val listState = rememberLazyListState()
    ConfigurePaging(listState, screenViewModel::updatePages)

    state?.DisplayContent(listState, onSelectAccountClicked)
}

@Composable
fun CardUiState.DisplayContent(
    listState: LazyListState,
    onSelectAccountClicked: (String) -> Unit
) = LazyColumn(state = listState) {
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
    onSelectAccountClicked: ((String) -> Unit)
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
    onSelectAccountClicked: ((String) -> Unit)
) {
    CardNameView(onSelectAccountClicked)

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
    onSelectAccountClicked: (String) -> Unit
) = Row(
    Modifier
        .clickable {
            onSelectAccountClicked(id)
        }
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

@Composable
private fun ConfigurePaging(
    listState: LazyListState,
    updatePages: ((PagingDirection) -> Unit)
) {
    val isScrollingUp = listState.isScrollingUp() // TODO implement isScrollingDown util

    val shouldLoadNewPage by remember {
        derivedStateOf {
            with(listState.layoutInfo) {
                totalItemsCount > 0 && itemsBeforeEnd <= 15
            }
        }
    }
    val shouldLoadOldPage by remember {
        derivedStateOf {
            with(listState.layoutInfo) {
                isScrollingUp && totalItemsCount > 0 && itemsAfterStart >= 15
            }
        }
    }

    LaunchedEffect(shouldLoadNewPage, shouldLoadOldPage) {
        val direction = if (shouldLoadNewPage) PagingDirection.Forward
        else if (shouldLoadOldPage) PagingDirection.Backward
        else null

        direction?.let(updatePages)
    }
}