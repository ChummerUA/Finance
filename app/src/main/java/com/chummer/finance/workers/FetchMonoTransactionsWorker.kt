package com.chummer.finance.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.chummer.domain.mono.fetchTransactions.FetchMonoTransactionsUseCase
import com.chummer.domain.mono.fetchTransactions.FetchTransactionsArgument
import com.chummer.domain.mono.fetchTransactions.FetchTransactionsResult
import com.chummer.finance.db.mono.lastFetchTime.upsertLastFetchTime.UpsertFetchTimeArgument
import com.chummer.finance.db.mono.lastFetchTime.upsertLastFetchTime.UpsertFetchTimeUseCase
import com.chummer.models.mapping.toLocalDateTime
import com.chummer.models.mapping.toUnixSecond
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDateTime

@HiltWorker
class FetchMonoTransactionsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val fetchUseCase: FetchMonoTransactionsUseCase,
    private val upsertFetchTimeUseCase: UpsertFetchTimeUseCase
) : CoroutineWorker(context, params) {
    private val data = params.inputData
    private val id = data.getString(ID_KEY) ?: argumentError(ID_KEY)

    private val type = data.getInt(FETCH_TYPE_KEY, -1).apply {
        if (this == -1) argumentError(FETCH_TYPE_KEY)
    }

    private val lastFetchTimeInUnix = data.getLong(LAST_FETCH_TIME_KEY, -1L).takeIf { it != -1L }
    private val lastFetchTime = lastFetchTimeInUnix?.toLocalDateTime()

    override suspend fun doWork(): Result {
        return try {
            val accountId = id.takeIf { type == FETCH_ACCOUNT_TYPE }
            val jarId = id.takeIf { type == FETCH_JAR_TYPE }

            var to = LocalDateTime.now()
            val from = lastFetchTime ?: to.minusMonths(1).withDayOfMonth(1)

            var fetchFinished = false
            while (!fetchFinished) {
                val fetchResult = fetchUseCase(
                    FetchTransactionsArgument(
                        accountId = accountId,
                        jarId = jarId,
                        from = from.toUnixSecond(),
                        to = to.toUnixSecond()
                    )
                )


                fetchFinished = fetchResult.fetchFullyCompleted

                if (fetchResult is FetchTransactionsResult.TransactionsReturned) {
                    to = fetchResult.oldestTransactionDateTime

                    upsertFetchTimeUseCase(
                        UpsertFetchTimeArgument(
                            accountId ?: jarId!!,
                            fetchResult.recentTransactionDateTime
                        )
                    )
                }
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("FetchMonoAccountsWorker", "Failed to fetch accounts", e)
            Result.failure()
        }
    }

    companion object {
        const val NAME = "fetch_mono_transactions"
        const val ID_KEY = "id"
        const val FETCH_TYPE_KEY = "fetch_type"
        const val LAST_FETCH_TIME_KEY = "last_fetch_time"

        const val FETCH_ACCOUNT_TYPE = 0
        const val FETCH_JAR_TYPE = 1
    }

}

@Throws(IllegalArgumentException::class)
private fun argumentError(key: String): Nothing {
    throw IllegalArgumentException(argumentErrorMessage(key))
}

private fun argumentErrorMessage(key: String) = "Missing $key argument"
