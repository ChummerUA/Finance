package com.chummer.finance.utils

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.semantics

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.noContentDescription() = semantics {
    invisibleToUser()
}

fun Modifier.contentDescription(
    contentDescription: String,
    mergeDescendants: Boolean = true
) = semantics(mergeDescendants) {
    this.contentDescription = contentDescription
}

fun Modifier.mergeContentDescription() = semantics(true) { }

const val TALKBACK_PAUSE_SYMBOL = "\n"
