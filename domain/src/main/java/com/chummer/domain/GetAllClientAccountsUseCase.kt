package com.chummer.domain

import com.chummer.finance.ChummerFinanceDatabase
import com.chummer.finance.db.mono.account.AccountListItem
import com.chummer.finance.db.mono.account.GetAccountsUseCase
import com.chummer.finance.db.mono.jar.GetJarsUseCase
import com.chummer.finance.db.mono.jar.JarListItem
import com.chummer.infrastructure.usecase.FlowUseCase
import com.chummer.models.None
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlin.coroutines.CoroutineContext

class GetAllClientAccountsUseCase(
    db: ChummerFinanceDatabase
) : FlowUseCase<None, List<ClientAccountListItem>>(KEY) {
    private val getAccountsUseCase = GetAccountsUseCase(db.accountQueries)
    private val getJarsUseCase = GetJarsUseCase(db.jarQueries)

    override val coroutineContext: CoroutineContext = Dispatchers.Default

    override fun invoke(input: None): Flow<List<ClientAccountListItem>> {
        val accountsFlow = getAccountsUseCase(None)
        val jarsFlow = getJarsUseCase(None)

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
}

const val KEY = "get_all_client_accounts"

sealed interface ClientAccountListItem {
    @JvmInline
    value class Jar(val info: JarListItem): ClientAccountListItem

    @JvmInline
    value class Account(val info: AccountListItem): ClientAccountListItem
}
