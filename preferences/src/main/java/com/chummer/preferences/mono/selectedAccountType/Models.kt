package com.chummer.preferences.mono.selectedAccountType

import androidx.datastore.preferences.core.intPreferencesKey

internal val selectedAccountKey = intPreferencesKey("selected_account_type_key")

const val ACCOUNT_TYPE_CARD = 0
const val ACCOUNT_TYPE_JAR = 1
const val ACCOUNT_TYPE_UNDEFINED = -1
