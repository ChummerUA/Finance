package com.chummer.finance.network.monobank

import com.chummer.infrastructure.network.HttpUseCase
import io.ktor.client.HttpClient
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

abstract class MonoRequestUseCase<Input, Output>(
    id: String,
    client: HttpClient,
    serializer: KSerializer<Output>
) : HttpUseCase<Input, Output>(id, client, serializer) {
    override val json: Json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }
}
