package com.chummer.domain.mono.selectedAccount

import com.chummer.infrastructure.usecase.ExecutableUseCase
import com.chummer.models.None
import com.chummer.preferences.mono.selectedAccount.SetSelectedAccountIdUseCase
import com.chummer.preferences.mono.selectedAccountType.SetSelectedAccountTypeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class SetSelectedAccountUseCase(
    val setSelectedAccountId: SetSelectedAccountIdUseCase,
    val setSelectedAccountType: SetSelectedAccountTypeUseCase
) : ExecutableUseCase<SelectedAccount, None>(KEY) {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override suspend fun execute(input: SelectedAccount) {
        return withContext(coroutineContext) {
            awaitAll(
                async { setSelectedAccountId(input.accountId) },
                async { setSelectedAccountType(input.accountType) }
            )
        }
    }

    private companion object {
        const val KEY = "SET_SELECTED_ACCOUNT"
    }
}
