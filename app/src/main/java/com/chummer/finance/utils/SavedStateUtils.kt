package com.chummer.finance.utils

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

fun <T : Any?> SavedStateHandle.getNullableStateFlow(
    key: String,
    initialValue: T?,
    scope: CoroutineScope
): MutableStateFlow<T?> {
    val currentValue = get<T>(key) ?: initialValue
    val stateFlow = MutableStateFlow(currentValue)
    scope.launch {
        stateFlow.collect {
            this@getNullableStateFlow[key] = it
        }
    }
    return stateFlow
}

fun <T : Any> SavedStateHandle.getStateFlow(
    key: String,
    initialValue: T,
    scope: CoroutineScope
): MutableStateFlow<T> {
    val currentValue = get<T>(key) ?: initialValue
    val stateFlow = MutableStateFlow(currentValue)
    scope.launch {
        stateFlow.collect {
            this@getStateFlow[key] = it
        }
    }
    return stateFlow
}
