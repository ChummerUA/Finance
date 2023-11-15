package com.chummer.finance.db.mono.lastFetchTime.upsertLastFetchTime

import app.cash.sqldelight.TransactionWithoutReturn
import com.chummer.infrastructure.db.useCases.transaction.withoutResult.DbTransactionUseCase
import mono.LastFetchTimeQueries

class UpsertFetchTimeUseCase(
    transacter: LastFetchTimeQueries
) : DbTransactionUseCase<UpsertFetchTimeArgument, LastFetchTimeQueries>(KEY, transacter) {
    override fun TransactionWithoutReturn.execute(argument: UpsertFetchTimeArgument) {
        val (id, newDateTime) = argument

        val current = transacter.getLastFetchTimeById(id).executeAsOne()
        if (newDateTime.isAfter(current))
            return

        transacter.upsertLastFetchTime(newDateTime, id)
    }

    private companion object {
        const val KEY = "UPSERT_FETCH_TIME"
    }
}
