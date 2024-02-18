package com.chummer.finance.db.mono.transaction.getTransaction

import java.time.LocalDate

data class GetTransactionsArgument(
    val accountId: String?,
    val search: String,
    val range: Pair<LocalDate, LocalDate>?,
    val time: Long,
    val pageSize: Long,
    val pages: Long,
    val shiftOnePageBack: Boolean = false
)
