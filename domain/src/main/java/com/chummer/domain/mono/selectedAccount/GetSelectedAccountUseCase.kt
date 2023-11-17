package com.chummer.domain.mono.selectedAccount

import com.chummer.infrastructure.usecase.ExecutableUseCase
import com.chummer.models.None
import com.chummer.preferences.mono.selectedAccount.GetSelectedAccountIdUseCase
import com.chummer.preferences.mono.selectedAccountType.ACCOUNT_TYPE_UNDEFINED
import com.chummer.preferences.mono.selectedAccountType.GetSelectedAccountTypeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class GetSelectedAccountUseCase(
    val getSelectedAccountId: GetSelectedAccountIdUseCase,
    val getSelectedAccountType: GetSelectedAccountTypeUseCase
) : ExecutableUseCase<None, SelectedAccount?>(KEY) {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override suspend fun execute(input: None): SelectedAccount? {
        return withContext(coroutineContext) {
            val accountId = async { getSelectedAccountId() }
            val accountType = async { getSelectedAccountType() }
            SelectedAccount(
                accountId.await(),
                accountType.await()
            ).takeIf { it.accountId.isNotBlank() && it.accountType != ACCOUNT_TYPE_UNDEFINED }
        }
    }

    private companion object {
        const val KEY = "GET_SELECTED_ACCOUNT"
    }
}
