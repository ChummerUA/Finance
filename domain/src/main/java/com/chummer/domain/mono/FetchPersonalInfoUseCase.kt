package com.chummer.domain.mono

import android.util.Log
import com.chummer.domain.mapping.mono.toDbModel
import com.chummer.finance.db.mono.account.UpsertAccountsUseCase
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
    val upsertJarsUseCase: UpsertJarsUseCase
) : ExecutableUseCase<None, None>(KEY) {
    override val coroutineContext: CoroutineContext = Dispatchers.Main

    override suspend fun execute(input: None) {
        withContext(coroutineContext) {
            Log.d(KEY, "Fetching info")
            val info = getPersonalInfoUseCase(None)
            Log.d(KEY, "Got info")
            awaitAll(
                async { upsertAccountsUseCase(info.accounts.map { it.toDbModel(info.clientId) }) },
                async { upsertJarsUseCase(info.jars.map { it.toDbModel(info.clientId) }) }
            )
            Log.d(KEY, "Finish upserting info")
        }
    }
}

private const val KEY = "fetch_accounts"
