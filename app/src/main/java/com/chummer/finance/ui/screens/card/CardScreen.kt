package com.chummer.finance.ui.screens.card

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chummer.finance.navigation.nodes.AccountNode
import com.chummer.finance.navigation.nodes.AccountNode.SelectAccount
import com.chummer.finance.ui.BalanceView
import com.chummer.finance.ui.Divider
import com.chummer.finance.ui.account.AccountInfoItem
import com.chummer.finance.ui.account.AccountUiModel
import com.chummer.finance.ui.account.AccountUiModel.Card
import com.chummer.finance.ui.account.AccountUiModel.FOP
import com.chummer.finance.ui.calendar.Calendar
import com.chummer.finance.ui.calendar.OnDatesSelected
import com.chummer.finance.ui.text.ClickableText
import com.chummer.finance.ui.text.ItemTitleText
import com.chummer.finance.ui.theme.AppTheme
import com.chummer.finance.ui.transaction.SearchBarState
import com.chummer.finance.ui.transaction.TransactionsSearchBar
import com.chummer.finance.ui.transaction.transactions
import com.chummer.finance.utils.ConfigurePaging
import com.chummer.finance.utils.OnClickListener
import com.chummer.finance.utils.OnTextChanged
import com.chummer.finance.utils.rememberStateWithLifecycle
import java.time.LocalDate

@Composable
fun CardScreen(
    navController: NavController,
    viewModel: CardViewModel = hiltViewModel()
) {
    val state by rememberStateWithLifecycle(viewModel.state)

    val onSelectAccountClicked: ((String) -> Unit) = remember {
        { id ->
            navController.navigate(SelectAccount.fullRoute) {
                val fullRoute = AccountNode.Card.route + "/{id}"
                Log.d("CardScreen", "Pop up to $fullRoute")
                popUpTo(fullRoute) {
                    inclusive = true
                }
            }
        }
    }

    val listState = rememberLazyListState()
    ConfigurePaging(15, listState, viewModel::updatePages)

    if (state == null) return
    state?.let {
        CardScreen(
            cardUiState = it,
            listState = listState,
            onSelectAccountClicked = onSelectAccountClicked,
            onSearchClicked = viewModel::activateSearch,
            onCategoriesClicked = viewModel::activateCategoriesSelection,
            onCalendarClicked = viewModel::activateCalendarSelection,
            onCancelClicked = viewModel::cancelSearch,
            onTextChanged = viewModel::updateSearchText,
            onDatesSelected = viewModel::updateSelectedDates
        )
    }
}

@Composable
private fun CardScreen(
    cardUiState: CardUiState,
    listState: LazyListState,
    onSelectAccountClicked: (String) -> Unit,
    onSearchClicked: OnClickListener,
    onCategoriesClicked: OnClickListener,
    onCalendarClicked: OnClickListener,
    onCancelClicked: OnClickListener,
    onTextChanged: OnTextChanged,
    onDatesSelected: OnDatesSelected
) = Column {
    with(cardUiState) {
        when (account) {
            is Card -> AccountInfo(account, onSelectAccountClicked)
            is FOP -> AccountInfo(account, onSelectAccountClicked)
        }
        TransactionsSearchBar(
            state = searchBarState,
            onSearchClicked = onSearchClicked,
            onCategoriesClicked = onCategoriesClicked,
            onCalendarClicked = onCalendarClicked,
            onCancelClicked = onCancelClicked,
            onTextChanged = onTextChanged
        )

        val today = remember { LocalDate.now() }
        when {
            searchBarState is SearchBarState.Expanded && searchBarState.isCalendar -> {
                Calendar(
                    today = today,
                    range = searchBarState.range,
                    onDatesSelected
                )
            }

            else -> {
                LazyColumn(state = listState) {
                    transactions(daysWithTransactions)
                }
            }
        }
    }
}

@Composable
private fun AccountInfo(
    card: Card,
    onSelectAccountClicked: ((String) -> Unit)
) = with(card) {
    CardName(card, onSelectAccountClicked)

    AccountInfoItem(cardNumber)

    val dividerPadding = PaddingValues(horizontal = 16.dp)
    Divider(dividerPadding)

    BalanceView(balance, creditInfo)

    Divider(dividerPadding)
}

@Composable
private fun AccountInfo(
    fop: FOP,
    onSelectAccountClicked: ((String) -> Unit)
) = with(fop) {
    CardName(fop, onSelectAccountClicked)

    AccountInfoItem(fop.iban)

    val dividerPadding = PaddingValues(horizontal = 16.dp)
    Divider(dividerPadding)

    BalanceView(balance, creditInfo)

    Divider(dividerPadding)
}

@Composable
private fun CardName(
    account: AccountUiModel,
    onSelectAccountClicked: (String) -> Unit
) = with(account) {
    Row(
        Modifier
            .clickable { onSelectAccountClicked(id) }
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
}
