package com.chummer.finance.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.chummer.domain.mono.operations.FetchOperationsInput
import com.chummer.domain.mono.operations.FetchOperationsUseCase
import com.chummer.finance.utils.toUnixSecond
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDateTime

@HiltWorker
class FetchMonoTransactionsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val fetchUseCase: FetchOperationsUseCase,
) : CoroutineWorker(context, params) {
    private val data = params.inputData
    private val id = data.getString(ID_KEY) ?: argumentError(ID_KEY)
    private val type = FetchOperationsType.fromInt(
        data.getInt(FETCH_TYPE_KEY, -1)
    )

    override suspend fun doWork(): Result {
        return try {
            val to = LocalDateTime.now()
            val from = to.minusMinutes(1).withDayOfMonth(1)

            val accountId = id.takeIf { type == FetchOperationsType.ACCOUNT }
            val jarId = id.takeIf { type == FetchOperationsType.JAR }

            fetchUseCase(
                FetchOperationsInput(
                    accountId = accountId,
                    jarId = jarId,
                    from = from.toUnixSecond(),
                    to = to.toUnixSecond()
                )
            )
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
    }

}

@Throws(IllegalArgumentException::class)
private fun argumentError(key: String): Nothing {
    throw IllegalArgumentException(argumentErrorMessage(key))
}

private fun argumentErrorMessage(key: String) = "Missing $key argument"

enum class FetchOperationsType(val value: Int) {
    ACCOUNT(0),
    JAR(1);

    companion object {
        fun fromInt(value: Int) = values().firstOrNull { it.value == value } ?: argumentError(
            FetchMonoTransactionsWorker.FETCH_TYPE_KEY
        )
    }
}
