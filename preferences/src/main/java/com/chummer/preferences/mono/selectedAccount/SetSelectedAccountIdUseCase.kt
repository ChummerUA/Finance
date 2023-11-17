package com.chummer.preferences.mono.selectedAccount

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import com.chummer.infrastructure.preferences.useCases.update.PreferencesUpdateUseCase

class SetSelectedAccountIdUseCase(
    context: Context
) : PreferencesUpdateUseCase<String>(KEY, context) {
    override val key: Preferences.Key<String> = selectedAccountKey

    companion object {
        const val KEY = "SET_SELECTED_ACCOUNT_ID"
    }
}
