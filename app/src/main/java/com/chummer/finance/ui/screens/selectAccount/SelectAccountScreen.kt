package com.chummer.finance.ui.screens.selectAccount

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
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
import androidx.navigation.NavController
import com.chummer.finance.navigation.nodes.AccountNode
import com.chummer.finance.ui.account.AccountUiListModel
import com.chummer.finance.ui.account.Display
import com.chummer.finance.ui.text.ItemTitleText
import com.chummer.finance.ui.theme.AppTheme
import com.chummer.finance.utils.rememberStateWithLifecycle
import com.chummer.finance.navigation.nodes.AccountNode.Card as CardNode
import com.chummer.finance.navigation.nodes.AccountNode.Jar as JarNode

@Composable
fun SelectAccountScreen(
    navController: NavController,
    screenViewModel: SelectAccountViewModel = hiltViewModel()
) {
    val state by rememberStateWithLifecycle(screenViewModel.state)

    val onItemClicked: (AccountUiListModel) -> Unit = remember {
        { item: AccountUiListModel ->
            screenViewModel.selectAccount(item)
            val route = when (item) {
                is AccountUiListModel.Card, is AccountUiListModel.FOP -> CardNode.resolve(item.id)
                is AccountUiListModel.Jar -> JarNode.resolve(item.id)
            }
            Log.d(TAG, "Navigating to $route")
            navController.navigate(route) {
                popUpTo(AccountNode.SelectAccount.fullRoute) {
                    inclusive = true
                }
            }
        }
    }

    state?.DisplayContent(onItemClicked = onItemClicked)
}

@Composable
private fun SelectAccountUiState.DisplayContent(
    onItemClicked: (AccountUiListModel) -> Unit
) {
    // TODO set title

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 0.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        group(cardGroup, onItemClicked = onItemClicked)
        group(fopGroup, onItemClicked = onItemClicked)
        group(jarGroup, onItemClicked = onItemClicked)
    }
}

private fun <T : AccountUiListModel> LazyGridScope.group(
    group: AccountsGroup<T>,
    onItemClicked: (AccountUiListModel) -> Unit
) {
    with(group) {
        if (groupShown) {
            header(title)
            items(items, key = { it.id }) {
                it.View(onItemClicked = onItemClicked)
            }
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
    when (this) {
        is AccountUiListModel.Card -> Display {
            onItemClicked(this)
        }

        is AccountUiListModel.FOP -> Display {
            onItemClicked(this)
        }

        is AccountUiListModel.Jar -> Display {
            onItemClicked(this)
        }
    }
}

private const val TAG = "SelectAccountScreen"
