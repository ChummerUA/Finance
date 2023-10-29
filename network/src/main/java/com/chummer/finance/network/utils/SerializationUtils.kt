package com.chummer.finance.network.utils

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> HttpResponse.deserializeBody(): T {
    return body()
}
