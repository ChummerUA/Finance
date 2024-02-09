package com.chummer.finance.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

class AppColors (
    primary: Color,
    backgroundPrimary: Color,
    backgroundSecondary: Color,
    textPrimary: Color,
    textSecondary: Color,
    onPrimary: Color,
    divider: Color,
    income: Color
) {
    var primary by mutableStateOf(primary)
        private set

    var backgroundPrimary by mutableStateOf(backgroundPrimary)
        private set

    var backgroundSecondary by mutableStateOf(backgroundSecondary)
        private set

    var textPrimary by mutableStateOf(textPrimary)
        private set

    var textSecondary by mutableStateOf(textSecondary)
        private set

    var onPrimary by mutableStateOf(onPrimary)
        private set

    var divider by mutableStateOf(divider)
        private set

    var income by mutableStateOf(income)
        private set
}

fun lightColors() = AppColors(
    primary = Color(0xFF033BFF),
    backgroundPrimary = Color(0xFFF0F0F0),
    backgroundSecondary = Color(0xFFE1E1E1),
    textPrimary = Color(0xFF292020),
    textSecondary = Color(0xFF969696),
    onPrimary = Color(0xFFFFFFFF),
    divider = Color(0xFFD3D3D3),
    income = Color(0xFF72A800)
)

internal val LocalColors = staticCompositionLocalOf{ lightColors() }
