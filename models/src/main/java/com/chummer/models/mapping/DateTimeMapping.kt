package com.chummer.models.mapping

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun Long.toLocalDateTime(): LocalDateTime {
    return Instant.ofEpochSecond(this).atZone(ZoneId.systemDefault()).toLocalDateTime()
}

fun LocalDateTime.toUnixSecond(): Long {
    return atZone(ZoneId.systemDefault()).toEpochSecond()
}
