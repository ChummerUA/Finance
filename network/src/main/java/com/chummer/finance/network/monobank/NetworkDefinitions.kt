package com.chummer.finance.network.monobank

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

const val baseUrl = "https://api.monobank.ua/"

fun provideMonoClient(
    token: String
) = HttpClient(OkHttp) {
    install(ContentNegotiation) {

        json(
            Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            }
        )
    }
    defaultRequest {
        url(baseUrl)
        header("X-Token", token)
    }
}
