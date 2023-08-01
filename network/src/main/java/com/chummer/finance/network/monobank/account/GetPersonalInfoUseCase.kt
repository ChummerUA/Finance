package com.chummer.finance.network.monobank.account

import com.chummer.infrastructure.network.ErrorMapper
import com.chummer.infrastructure.network.HttpUseCase
import com.chummer.infrastructure.network.RequestDefinition
import com.chummer.infrastructure.network.ResultMapper
import com.chummer.models.None
import com.chummer.networkmodels.mono.AccountResponse
import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod

class GetPersonalInfoUseCase(
    client: HttpClient
) : HttpUseCase<None, AccountResponse, Throwable>(key, client) {

    override val definition = RequestDefinition(
        subPath = "personal/client-info",
        method = HttpMethod.Get
    )

    override val responseMapper: ResultMapper<AccountResponse> = ResultMapper.JsonResultMapper()

    override val errorMapper: ErrorMapper<Throwable> = object : ErrorMapper <Throwable>() {
        override suspend fun HttpResponse.toError(): Throwable {
            TODO("Implement error handling")
        }
    }
}

private const val key = "get_personal_info_usecase"
