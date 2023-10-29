package com.chummer.finance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.chummer.finance.ui.screens.account.selectAccount.SelectAccountScreen
import com.chummer.finance.ui.theme.AppTheme

@Composable
fun Navigation(navController: NavHostController, start: NodeDefinition) = NavHost(
    navController = navController,
    startDestination = start.route,
    Modifier
        .background(AppTheme.colors.backgroundPrimary)
        .fillMaxSize(1f)
) {
    addNode(AccountScreens.SelectAccount)
}

object AccountScreens {
    object SelectAccount : NodeDefinition {
        override val route: String = "select_account"

        @Composable
        override fun Screen() = SelectAccountScreen()
    }
}

interface NodeDefinition {
    val route: String

    @Composable
    fun Screen()
}

fun NavGraphBuilder.addNode(definition: NodeDefinition) {
    composable(definition.route) { definition.Screen() }
}
