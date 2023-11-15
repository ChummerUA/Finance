package com.chummer.finance.db.mono.lastFetchTime.upsertLastFetchTime

import java.time.LocalDateTime

data class UpsertFetchTimeArgument(
    val id: String,
    val time: LocalDateTime
)
