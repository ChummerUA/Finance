package com.chummer.finance.db.mono.lastFetchTime

import app.cash.sqldelight.Query
import com.chummer.infrastructure.db.useCases.item.DbItemOrNullUseCase
import mono.LastFetchTimeQueries
import java.time.LocalDateTime

class GetLastTransactionsFetchTimeUseCase(
    transacter: LastFetchTimeQueries
) : DbItemOrNullUseCase<String, LocalDateTime, LastFetchTimeQueries>(
    KEY, transacter
) {

    private companion object {
        const val KEY = "GET_LAST_TRANSACTIONS_FETCH_TIME"
    }

    override fun LastFetchTimeQueries.getQuery(argument: String): Query<LocalDateTime> {
        return getLastFetchTimeById(argument)
    }
}
