package com.chummer.finance.db.mono.lastFetchTime.upsertLastFetchTime

import android.util.Log
import app.cash.sqldelight.TransactionWithoutReturn
import com.chummer.infrastructure.db.useCases.transaction.withoutResult.DbTransactionUseCase
import mono.LastFetchTimeQueries

class UpsertFetchTimeUseCase(
    transacter: LastFetchTimeQueries,
) : DbTransactionUseCase<UpsertFetchTimeArgument, LastFetchTimeQueries>(KEY, transacter) {
    override fun TransactionWithoutReturn.execute(argument: UpsertFetchTimeArgument) {
        val (id, newDateTime) = argument

        val current = transacter.getLastFetchTimeById(id).executeAsOneOrNull()
        val shouldUpsert = current == null || newDateTime.isAfter(current)
        Log.d(KEY, "Upserting fetch date. Current - $current, new - $newDateTime")

        if (!shouldUpsert)
            return

        transacter.upsertLastFetchTime(newDateTime, id)
    }

    private companion object {
        const val KEY = "UPSERT_FETCH_TIME"
    }
}
