package com.chummer.preferences.mono.lastInfoFetchTime

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import com.chummer.infrastructure.preferences.useCases.update.PreferencesUpdateUseCase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SetLastMonoAccountsFetchTimeUseCase(
    context: Context
) : PreferencesUpdateUseCase<String>(KEY, context) {
    override val key: Preferences.Key<String> = lastMonoAccountsFetchTimeKey

    suspend operator fun invoke(now: LocalDateTime) {
        invoke(now.format(DateTimeFormatter.ISO_DATE_TIME))
    }
}

private const val KEY = "set_last_mono_accounts_fetch_time"
