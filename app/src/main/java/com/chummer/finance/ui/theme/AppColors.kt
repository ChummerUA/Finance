package com.chummer.finance.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

class AppColors (
    backgroundPrimary: Color,
    backgroundSecondary: Color,
    textPrimary: Color,
    textSecondary: Color,
    divider: Color,
    income: Color
) {
    var backgroundPrimary by mutableStateOf(backgroundPrimary)
        private set

    var backgroundSecondary by mutableStateOf(backgroundSecondary)
        private set

    var textPrimary by mutableStateOf(textPrimary)
        private set

    var textSecondary by mutableStateOf(textSecondary)
        private set

    var divider by mutableStateOf(divider)
        private set

    var income by mutableStateOf(income)
        private set
}

private val lightBackgroundPrimaryColor = Color(0xFFF0F0F0)
private val lightBackgroundSecondaryColor = Color(0xFFE1E1E1)
private val lightTextPrimaryColor = Color(0xFF292020)
private val lightTextSecondaryColor = Color(0xFF969696)
private val lightDividerColor = Color(0xFFD3D3D3)
private val lightIncomeColor = Color(0xFF72A800)

fun lightColors() = AppColors(
    backgroundPrimary = lightBackgroundPrimaryColor,
    backgroundSecondary = lightBackgroundSecondaryColor,
    textPrimary = lightTextPrimaryColor,
    textSecondary = lightTextSecondaryColor,
    divider = lightDividerColor,
    income = lightIncomeColor
)

internal val LocalColors = staticCompositionLocalOf{ lightColors() }
