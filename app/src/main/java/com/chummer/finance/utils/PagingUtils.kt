package com.chummer.finance.utils

import androidx.compose.foundation.lazy.LazyListLayoutInfo

val LazyListLayoutInfo.itemsAfterStart
    get() = visibleItemsInfo.last().index + 1

val LazyListLayoutInfo.itemsBeforeEnd
    get() = totalItemsCount - (visibleItemsInfo.last().index + 1)

sealed interface PagingDirection {
    data object Forward : PagingDirection

    data object Backward : PagingDirection
}
