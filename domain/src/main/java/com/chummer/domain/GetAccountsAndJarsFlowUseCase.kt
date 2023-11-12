package com.chummer.domain

import com.chummer.finance.db.mono.account.AccountListItem
import com.chummer.finance.db.mono.account.GetAccountsFlowUseCase
import com.chummer.finance.db.mono.jar.GetJarsFlowUseCase
import com.chummer.finance.db.mono.jar.JarListItem
import com.chummer.infrastructure.usecase.FlowUseCase
import com.chummer.models.None
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlin.coroutines.CoroutineContext

class GetAccountsAndJarsFlowUseCase(
    private val getAccountsFlowUseCase: GetAccountsFlowUseCase,
    private val getJarsFlowUseCase: GetJarsFlowUseCase
) : FlowUseCase<None, List<ClientAccountListItem>>(KEY) {

    override val coroutineContext: CoroutineContext = Dispatchers.Default

    override fun invoke(input: None): Flow<List<ClientAccountListItem>> {
        val accountsFlow = getAccountsFlowUseCase(None)
        val jarsFlow = getJarsFlowUseCase(None)

        return combine(
            accountsFlow,
            jarsFlow
        ) { accounts, jars ->
            val result = mutableListOf<ClientAccountListItem>()
            result.addAll(
                accounts.map {
                    ClientAccountListItem.Account(it)
                }
            )
            result.addAll(
                jars.map {
                    ClientAccountListItem.Jar(it)
                }
            )

            result
        }
    }

    private companion object {
        const val KEY = "get_all_client_accounts"
    }
}

sealed interface ClientAccountListItem {
    @JvmInline
    value class Jar(val info: JarListItem): ClientAccountListItem

    @JvmInline
    value class Account(val info: AccountListItem): ClientAccountListItem
}
