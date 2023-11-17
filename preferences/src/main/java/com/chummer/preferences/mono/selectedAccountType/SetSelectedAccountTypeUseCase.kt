package com.chummer.preferences.mono.selectedAccountType

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import com.chummer.infrastructure.preferences.useCases.update.PreferencesUpdateUseCase

class SetSelectedAccountTypeUseCase(
    context: Context
) : PreferencesUpdateUseCase<Int>(KEY, context) {
    override val key: Preferences.Key<Int> = selectedAccountKey

    companion object {
        const val KEY = "SET_SELECTED_ACCOUNT_TYPE"
    }
}
