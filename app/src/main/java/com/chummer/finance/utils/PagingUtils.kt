package com.chummer.finance.utils

import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

val LazyListLayoutInfo.itemsAfterStart
    get() = visibleItemsInfo.last().index + 1

val LazyListLayoutInfo.itemsBeforeEnd
    get() = totalItemsCount - (visibleItemsInfo.last().index + 1)

sealed interface PagingDirection {
    data object Forward : PagingDirection

    data object Backward : PagingDirection
}

@Composable
fun ConfigurePaging(
    itemsOffset: Int,
    listState: LazyListState,
    updatePages: OnPageLoad
) {
    val isScrollingUp = listState.isScrollingUp() // TODO implement isScrollingDown util

    val shouldLoadNewPage by remember {
        derivedStateOf {
            with(listState.layoutInfo) {
                totalItemsCount > 0 && itemsBeforeEnd <= itemsOffset && listState.isScrollInProgress
            }
        }
    }
    val shouldLoadOldPage by remember {
        derivedStateOf {
            with(listState.layoutInfo) {
                isScrollingUp && totalItemsCount > 0 && itemsAfterStart >= itemsOffset
            }
        }
    }

    LaunchedEffect(shouldLoadNewPage, shouldLoadOldPage) {
        val direction = if (shouldLoadNewPage) PagingDirection.Forward
        else if (shouldLoadOldPage) PagingDirection.Backward
        else null

        direction?.let(updatePages)
    }
}

typealias OnPageLoad = ((PagingDirection) -> Unit)
