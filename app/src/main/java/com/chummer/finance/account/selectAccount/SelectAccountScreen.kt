package com.chummer.finance.account.selectAccount

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chummer.finance.account.CardListItemView
import com.chummer.finance.account.FopListItemView
import com.chummer.finance.account.JarListItemView
import com.chummer.finance.text.ItemTitleText
import com.chummer.finance.theme.AppTheme
import com.chummer.finance.utils.rememberStateWithLifecycle

@Composable
fun SelectAccountScreen(
    screenViewModel: SelectAccountViewModel = hiltViewModel()
) {
    val state by rememberStateWithLifecycle(screenViewModel.state)

    val onItemClicked: (AccountUiListModel) -> Unit = remember {
        { item: AccountUiListModel ->
            screenViewModel.selectAccount(item)
        }
    }

    state?.DisplayContent(onItemClicked = onItemClicked)
}

@Composable
private fun SelectAccountUiState.DisplayContent(
    onItemClicked: (AccountUiListModel) -> Unit
) {
    // TODO set title

    // TODO set content spacing
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 0.dp
        )
    ) {
        header(accountsTitle)
        items(accounts, key = { it.id }) { item ->
            item.View(onItemClicked = onItemClicked)
        }

        header(jarsTitle)
        items(jars, key = { it.id }) { item ->
            item.View(onItemClicked = onItemClicked)
        }
    }
}

private fun LazyGridScope.header(
    title: String
) {
    item(span = { GridItemSpan(this.maxLineSpan) }) {
        val colors = AppTheme.colors
        ItemTitleText(text = title, color = colors.textPrimary)
    }
}

@Composable
private fun AccountUiListModel.View(
    onItemClicked: (AccountUiListModel) -> Unit
) {
    when(this) {
        is AccountUiListModel.Card -> CardListItemView(card = this) {
            onItemClicked(this)
        }
        is AccountUiListModel.FOP -> FopListItemView(fop = this) {
            onItemClicked(this)
        }
        is AccountUiListModel.Jar -> JarListItemView(card = this) {
            onItemClicked(this)
        }
    }
}
