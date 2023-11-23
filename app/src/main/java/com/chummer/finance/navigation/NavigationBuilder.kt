package com.chummer.finance.navigation

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

data class NavigationBuilder(
    private val navController: NavController,
    private val graphBuilder: NavGraphBuilder
) {
    fun addNode(definition: NodeDefinition) {
        graphBuilder.apply {
            Log.d(
                "NavigationBuilder",
                "Adding node: ${definition.fullRoute} with arguments: ${definition.arguments}"
            )
            composable(
                definition.fullRoute,
                definition.arguments
            ) {
                definition.Screen(navController)
            }
        }
    }
}
