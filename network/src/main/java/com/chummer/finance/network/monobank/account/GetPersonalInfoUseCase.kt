package com.chummer.finance.network.monobank.account

import com.chummer.finance.network.monobank.MonoRequestUseCase
import com.chummer.infrastructure.network.RequestDefinition
import com.chummer.models.None
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import kotlinx.serialization.serializer

class GetPersonalInfoUseCase(
    client: HttpClient
) : MonoRequestUseCase<None, AccountResponse>(KEY, client, serializer()) {

    override val definition = RequestDefinition(
        subPath = "personal/client-info",
        method = HttpMethod.Get
    )
}

private const val KEY = "get_personal_info"
