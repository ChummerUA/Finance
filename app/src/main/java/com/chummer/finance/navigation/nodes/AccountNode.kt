package com.chummer.finance.navigation.nodes

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.chummer.finance.navigation.NodeDefinition
import com.chummer.finance.ui.screens.card.CardScreen
import com.chummer.finance.ui.screens.selectAccount.SelectAccountScreen

sealed interface AccountNode : NodeDefinition {
    data object SelectAccount : AccountNode {
        override val route: String = "select_account"

        @Composable
        override fun Screen(
            navController: NavController
        ) = SelectAccountScreen(
            navController
        )
    }

    data object Card : AccountNode {
        override val route: String = "card"

        override val arguments: List<NamedNavArgument> = listOf(
            navArgument("id") { type = NavType.StringType }
        )

        @Composable
        override fun Screen(navController: NavController) = CardScreen(navController)

        fun resolve(id: String) = "$route/$id"
    }

    data object Jar : AccountNode {
        override val route: String = "jar"

        override val arguments: List<NamedNavArgument> = listOf(
            navArgument("id") { type = NavType.StringType }
        )

        @Composable
        override fun Screen(navController: NavController) {
//            JarScreen(navController)
        }

        fun resolve(id: String) = "$route/$id"
    }

    companion object {
        val all
            get() = listOf(SelectAccount, Card, Jar)
    }
}
