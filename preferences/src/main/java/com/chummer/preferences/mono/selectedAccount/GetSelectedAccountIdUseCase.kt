package com.chummer.preferences.mono.selectedAccount

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import com.chummer.infrastructure.preferences.useCases.get.PreferencesGetUseCase

class GetSelectedAccountIdUseCase(
    context: Context
) : PreferencesGetUseCase<String>(KEY, context) {
    override val defaultValue: String = ""
    override val key: Preferences.Key<String> = selectedAccountKey

    companion object {
        const val KEY = "GET_SELECTED_ACCOUNT_ID"
    }
}
