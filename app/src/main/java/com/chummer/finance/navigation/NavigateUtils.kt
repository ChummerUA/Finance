package com.chummer.finance.navigation

import androidx.navigation.NavController

fun NavController.navigateTo(
    route: String,
    nodeToPopUpTo: NodeDefinition,
    inclusive: Boolean = false
) {
    navigate(route) {
        popUpTo(nodeToPopUpTo.fullRoute) {
            this.inclusive = inclusive
        }
    }
}
