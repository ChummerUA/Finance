package com.chummer.finance.utils

import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

internal inline fun <reified Worker : CoroutineWorker> WorkManager.scheduleFetchWorker(
    workName: String,
    data: Data?,
    delay: Duration,
    lastFetchTime: LocalDateTime?
) {
    val now = LocalDateTime.now()
    Log.d(TAG, "Last fetch time: $lastFetchTime")
    val nextAvailableTime = lastFetchTime?.plusSeconds(delay.inWholeSeconds)
    Log.d(TAG, "Next fetch time: $nextAvailableTime")
    val shouldDelay = lastFetchTime != LocalDateTime.MIN && nextAvailableTime?.isAfter(now) == true
    Log.d(TAG, "Should delay: $shouldDelay")

    val request = OneTimeWorkRequestBuilder<Worker>()
        .apply {
            if (data != null)
                setInputData(data)

            if (shouldDelay) {
                val between = ChronoUnit.SECONDS.between(nextAvailableTime, now)
                val fetchDelay = between.seconds.absoluteValue
                Log.d(TAG, "Delay in seconds: ${fetchDelay.inWholeSeconds}")
                setInitialDelay(fetchDelay.toJavaDuration())
            }
        }
        .build()
    enqueueUniqueWork(
        workName,
        ExistingWorkPolicy.REPLACE,
        request
    )
}

private const val TAG = "WorkerUtils"
