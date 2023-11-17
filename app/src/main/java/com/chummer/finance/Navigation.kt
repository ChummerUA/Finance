package com.chummer.finance

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.chummer.finance.ui.screens.account.AccountScreen
import com.chummer.finance.ui.screens.account.selectAccount.SelectAccountScreen
import com.chummer.finance.ui.screens.splash.SplashScreen
import com.chummer.finance.ui.theme.AppTheme

@Composable
fun Navigation(navController: NavHostController, start: NodeDefinition) = NavHost(
    navController = navController,
    startDestination = start.route,
    Modifier
        .background(AppTheme.colors.backgroundPrimary)
        .fillMaxSize(1f)
) {
    AccountNode.all.forEach {
        addNode(it, navController)
    }
    addNode(SplashNode, navController)
}

sealed interface AccountNode : NodeDefinition {
    data object SelectAccount : AccountNode {
        override val route: String = "select_account"

        @Composable
        override fun Screen(navController: NavController) = SelectAccountScreen(navController)
    }

    data object Card : AccountNode {
        override val route: String = "card"

        override val arguments: List<NamedNavArgument> = listOf(
            navArgument("id") { type = NavType.StringType }
        )

        @Composable
        override fun Screen(navController: NavController) = AccountScreen(navController)

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
            get() = listOf(SelectAccount, Card)
    }
}

object SplashNode : NodeDefinition {
    override val route: String = "start"

    @Composable
    override fun Screen(navController: NavController) {
        SplashScreen(navController = navController)
    }
}

interface NodeDefinition {
    val route: String

    val fullRoute: String
        get() {
            var result = route
            val argumentsString = arguments.toArgumentsString()
            if (argumentsString.isNotBlank())
                result += "/${argumentsString}"
            return result
        }

    val arguments: List<NamedNavArgument>
        get() = emptyList()

    @Composable
    fun Screen(navController: NavController)

    private fun List<NamedNavArgument>.toArgumentsString(): String {
        val (requiredArgs, optionalArgs) = this.partition { !it.argument.isDefaultValuePresent }
        val requiredArgsString = requiredArgs.joinToString(separator = "/") { "{${it.name}}" }
        val optionalArgsString = optionalArgs.joinToString(separator = "&") {
            val name = it.name
            "$name={$name}"
        }

        var result = ""
        if (requiredArgs.any()) {
            result += requiredArgsString
            if (optionalArgs.any())
                result += "/"
        }
        if (optionalArgs.any())
            result += optionalArgsString

        return result
    }
}

fun NavGraphBuilder.addNode(
    definition: NodeDefinition,
    controller: NavController
) {
    Log.d(
        "Navigation",
        "Adding node: ${definition.fullRoute} with arguments: ${definition.arguments}"
    )
    composable(
        definition.fullRoute,
        definition.arguments
    ) {
        definition.Screen(controller)
    }
}
