package com.chummer.finance.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import com.chummer.finance.ui.theme.LocalColors
import com.chummer.finance.utils.OnClickListener
import kotlinx.serialization.Serializable

@Composable
fun IconView(
    id: Int,
    state: IconState = IconState.Default,
    size: Dp,
    contentDescription: String? = null,
    onClicked: OnClickListener? = null
) {
    val tint = when (state) {
        IconState.Selected -> LocalColors.current.primary
        else -> LocalColors.current.backgroundSecondary
    }
    val vector = ImageVector.vectorResource(id = id)
    val modifier = Modifier
        .size(size)
        .let { lModifier ->
            onClicked?.let { lModifier.clickable(true, onClick = onClicked) } ?: lModifier
        }
    Icon(
        imageVector = vector,
        contentDescription = contentDescription,
        tint = tint,
        modifier = modifier
    )
}

@Serializable
sealed interface IconState {
    val selected
        get() = this is Selected

    val default
        get() = this is Default

    val disabled
        get() = this is Disabled

    data object Default : IconState

    data object Selected : IconState

    data object Disabled : IconState
}

fun Boolean.selectedOrDefault() = if (this) IconState.Selected else IconState.Default

fun Boolean.defaultOrDisabled() = if (this) IconState.Default else IconState.Disabled
