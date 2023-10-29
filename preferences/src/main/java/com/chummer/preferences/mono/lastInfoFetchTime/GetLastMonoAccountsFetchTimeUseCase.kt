package com.chummer.preferences.mono.lastInfoFetchTime

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import com.chummer.infrastructure.preferences.useCases.get.PreferencesGetUseCase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class GetLastMonoAccountsFetchTimeUseCase(
    context: Context
) : PreferencesGetUseCase<String>(KEY, context) {
    override val defaultValue: String = LocalDateTime.MIN.format(DateTimeFormatter.ISO_DATE_TIME)
    override val key: Preferences.Key<String> = lastMonoAccountsFetchTimeKey
}

private const val KEY = "get_last_mono_accounts_fetch_time"
