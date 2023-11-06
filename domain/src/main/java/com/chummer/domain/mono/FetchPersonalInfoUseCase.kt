package com.chummer.domain.mono

import android.util.Log
import com.chummer.domain.mapping.mono.toDbModel
import com.chummer.finance.db.mono.account.DeleteAccountsThatAreNotInListUseCase
import com.chummer.finance.db.mono.account.UpsertAccountsUseCase
import com.chummer.finance.db.mono.jar.DeleteJarsThatAreNotInListUseCase
import com.chummer.finance.db.mono.jar.UpsertJarsUseCase
import com.chummer.finance.network.monobank.account.GetPersonalInfoUseCase
import com.chummer.infrastructure.usecase.ExecutableUseCase
import com.chummer.models.None
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class FetchPersonalInfoUseCase(
    val getPersonalInfoUseCase: GetPersonalInfoUseCase,
    val upsertAccountsUseCase: UpsertAccountsUseCase,
    val upsertJarsUseCase: UpsertJarsUseCase,
    val deleteAccountsThatAreNotInListUseCase: DeleteAccountsThatAreNotInListUseCase,
    val deleteJarsThatAreNotInListUseCase: DeleteJarsThatAreNotInListUseCase
) : ExecutableUseCase<None, None>(KEY) {
    override val coroutineContext: CoroutineContext = Dispatchers.Main

    override suspend fun execute(input: None) {
        withContext(coroutineContext) {
            Log.d(KEY, "Fetching info")
            val info = getPersonalInfoUseCase(None)
            val accounts = info.accounts?.map { it.toDbModel(info.clientId) } ?: emptyList()
            val jars = info.jars?.map { it.toDbModel(info.clientId) } ?: emptyList()
            val accountIds = accounts.map { it.id }
            val jarIds = jars.map { it.id }
            Log.d(KEY, "Got info")

            awaitAll(
                async { upsertAccountsUseCase(accounts) },
                async { upsertJarsUseCase(jars) },
                async { deleteAccountsThatAreNotInListUseCase(accountIds) },
                async { deleteJarsThatAreNotInListUseCase(jarIds) }
            )
            // TODO remove deleted accounts/jars from db
            Log.d(KEY, "Finish upserting info")
        }
    }
}

private const val KEY = "fetch_accounts"
