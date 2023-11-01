package com.chummer.finance.network.monobank.account

import com.chummer.finance.network.utils.deserializeBody
import com.chummer.infrastructure.network.HttpUseCase
import com.chummer.infrastructure.network.RequestDefinition
import com.chummer.models.None
import com.chummer.networkmodels.mono.AccountResponse
import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod

class GetPersonalInfoUseCase(
    client: HttpClient
) : HttpUseCase<None, AccountResponse>(KEY, client) {

    override val definition = RequestDefinition(
        subPath = "personal/client-info",
        method = HttpMethod.Get
    )

    override suspend fun HttpResponse.deserialize(): AccountResponse {
        return deserializeBody()
    }
}

private const val KEY = "get_personal_info"
