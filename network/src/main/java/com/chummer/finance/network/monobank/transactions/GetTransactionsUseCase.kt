package com.chummer.finance.network.monobank.transactions

import com.chummer.finance.network.monobank.MonoRequestUseCase
import com.chummer.infrastructure.network.RequestDefinition
import com.chummer.models.mapping.toUnixSecond
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import kotlinx.serialization.serializer

class GetTransactionsUseCase(
    client: HttpClient
) : MonoRequestUseCase<GetTransactionsParameters, List<Transaction>>(KEY, client, serializer()) {
    override val definition = RequestDefinition(
        subPath = "personal/statement",
        method = HttpMethod.Get
    )

    override fun HttpRequestBuilder.configureRequest(parameter: GetTransactionsParameters) {
        assert(parameter.from.isBefore(parameter.to))
        assert(parameter.account.isNotBlank())

        val from = parameter.from.toUnixSecond()
        val to = parameter.to.toUnixSecond()
        this.method = definition.method
        url("${definition.subPath}/${parameter.account}/$from/$to")
    }
}

const val KEY = "get_transactions"
