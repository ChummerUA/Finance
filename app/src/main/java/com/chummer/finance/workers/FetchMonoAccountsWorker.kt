package com.chummer.finance.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.chummer.domain.mono.fetchAccounts.FetchMonoAccountsUseCase
import com.chummer.models.None
import com.chummer.preferences.mono.lastInfoFetchTime.SetLastMonoAccountsFetchTimeUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDateTime

@HiltWorker
class FetchMonoAccountsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val fetchUseCase: FetchMonoAccountsUseCase,
    private val setLastMonoAccountsFetchTimeUseCase: SetLastMonoAccountsFetchTimeUseCase
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return try {
            fetchUseCase(None)
            setLastMonoAccountsFetchTimeUseCase(LocalDateTime.now())
            Result.success()
        } catch (e: Exception) {
            Log.e("FetchMonoAccountsWorker", "Failed to fetch accounts", e)
            Result.failure()
        }
    }

    companion object {
        const val name = "fetch_mono_accounts"
    }
}
