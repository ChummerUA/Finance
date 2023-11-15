package com.chummer.finance.db.adapters

import app.cash.sqldelight.ColumnAdapter
import com.chummer.models.mapping.toLocalDateTime
import com.chummer.models.mapping.toUnixSecond
import java.time.LocalDateTime

object LocalDateTimeAdapter : ColumnAdapter<LocalDateTime, Long> {
    override fun decode(databaseValue: Long): LocalDateTime {
        return databaseValue.toLocalDateTime()
    }

    override fun encode(value: LocalDateTime): Long {
        return value.toUnixSecond()
    }
}
