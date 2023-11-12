package com.chummer.finance.ui.screens.account

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
import com.chummer.finance.ui.BalanceView
import com.chummer.finance.ui.DividerView
import com.chummer.finance.ui.account.AccountUiModel
import com.chummer.finance.ui.account.AccountUiState
import com.chummer.finance.ui.account.Display
import com.chummer.finance.ui.text.ClickableText
import com.chummer.finance.ui.text.ItemTitleText
import com.chummer.finance.ui.theme.AppTheme
import com.chummer.finance.ui.transaction.transactions
import com.chummer.finance.utils.rememberStateWithLifecycle

@Composable
fun AccountScreen(
    navController: NavController,
    screenViewModel: AccountViewModel = hiltViewModel()
) {
    val state by rememberStateWithLifecycle(screenViewModel.state)

    state?.DisplayContent()
}

@Composable
fun AccountUiState.DisplayContent() {
    LazyColumn {
        item(key = "account") {
            account.Display()
        }
        transactions(daysWithTransactions)
    }
}

@Composable
fun AccountUiModel.Display() {
    Row(
        Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        ItemTitleText(
            text = title,
            color = AppTheme.colors.textPrimary,
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight()
        )
        ClickableText(text = selectAllText, color = AppTheme.colors.primary)
    }

    cardNumber.Display()
    iban.Display()

    val dividerPadding = remember { PaddingValues(horizontal = 16.dp) }
    DividerView(
        paddingValues = dividerPadding
    )

    BalanceView(balance)

    DividerView(
        paddingValues = dividerPadding
    )
}
