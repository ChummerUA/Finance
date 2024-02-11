package com.chummer.finance.utils

import java.time.DayOfWeek
import java.time.LocalDate

fun LocalDate.withDayOfWeek(dayOfWeek: DayOfWeek): LocalDate {
    return minusDays(this.dayOfWeek.value.toLong()).plusDays(dayOfWeek.value.toLong())
}

fun LocalDate.withDayOfWeek(dayOfWeek: Int): LocalDate = withDayOfWeek(DayOfWeek.of(dayOfWeek))
