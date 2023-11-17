package com.chummer.preferences.mono.selectedAccountType

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import com.chummer.infrastructure.preferences.useCases.get.PreferencesGetUseCase

class GetSelectedAccountTypeUseCase(
    context: Context
) : PreferencesGetUseCase<Int>(KEY, context) {
    override val defaultValue: Int = ACCOUNT_TYPE_UNDEFINED
    override val key: Preferences.Key<Int> = selectedAccountKey

    companion object {
        const val KEY = "GET_SELECTED_ACCOUNT_TYPE"
    }
}
