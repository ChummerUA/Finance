package com.chummer.finance.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.chummer.finance.navigation.nodes.AccountNode
import com.chummer.finance.ui.screens.splash.SplashScreen
import com.chummer.finance.ui.theme.AppTheme

@Composable
fun Navigation(navController: NavHostController, start: NodeDefinition) = NavHost(
    navController = navController,
    startDestination = start.route,
    Modifier
        .windowInsetsPadding(WindowInsets.statusBars)
        .background(AppTheme.colors.backgroundPrimary)
        .fillMaxSize(1f)
) {
    val navBuilder = NavigationBuilder(navController, this)
    navBuilder.apply {
        addNode(SplashNode)
        AccountNode.all.forEach(this::addNode)
    }
}

data object SplashNode : NodeDefinition {
    override val route: String = "start"

    @Composable
    override fun Screen(
        navController: NavController
    ) {
        SplashScreen(navController)
    }
}
