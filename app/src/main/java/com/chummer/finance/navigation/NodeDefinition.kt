package com.chummer.finance.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController

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
