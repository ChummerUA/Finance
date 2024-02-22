package com.chummer.finance.network.monobank

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header

const val baseUrl = "https://api.monobank.ua/"

fun provideMonoClient(
    token: String
) = HttpClient(OkHttp) {
    defaultRequest {
        url(baseUrl)
        header("X-Token", token)
    }
}
