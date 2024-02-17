package com.chummer.finance.utils

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.semantics

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.noContentDescription() = semantics {
    invisibleToUser()
}
