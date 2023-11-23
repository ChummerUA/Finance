package com.chummer.finance.ui.screens.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chummer.finance.navigation.SplashNode
import com.chummer.finance.navigation.nodes.AccountNode
import com.chummer.finance.ui.screens.splash.SplashViewModel.NavigationEvent.Card
import com.chummer.finance.ui.screens.splash.SplashViewModel.NavigationEvent.Jar
import com.chummer.finance.ui.screens.splash.SplashViewModel.NavigationEvent.SelectAccount
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    LaunchedEffect("navigation_event") {
        launch {
            val route = when (val event = viewModel.navigationFlow.first()) {
                is Card -> AccountNode.Card.resolve(event.id)
                is Jar -> AccountNode.Jar.resolve(event.id)
                is SelectAccount -> AccountNode.SelectAccount.fullRoute
            }
            navController.navigate(route) {
                popUpTo(SplashNode.fullRoute) {
                    inclusive = true
                }
            }
        }
    }
}
