package com.chummer.finance.account.selectAccount

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.chummer.domain.GetAllClientAccountsUseCase
import com.chummer.finance.utils.stateInViewModelScope
import com.chummer.models.None
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest

@HiltViewModel
class SelectAccountViewModel(
    application: Application,
    getAccountsUseCase: GetAllClientAccountsUseCase
//    private val setSelectedAccountUseCase: Any = TODO()
) : AndroidViewModel(application) {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val allAccountsFlow = getAccountsUseCase(None).mapLatest { list ->
        list.map { it.toUiModel(application.applicationContext) }
    }
    private val title = application.getString(TODO())
    private val accountsTitle = application.getString(TODO())
    private val accountsFlow = allAccountsFlow.map { list ->
        list.filter { it !is AccountUiListModel.Jar }
    }
    private val jarsTitle: String = application.getString(TODO())
    private val jarsFlow = allAccountsFlow.map {
        it.filterIsInstance<AccountUiListModel.Jar>()
    }

    val state = combine(
        accountsFlow,
        jarsFlow
    ) { accounts, jars ->
        SelectAccountUiState(
            title = title,
            backButtonVisible = false,
            accountsTitle = accountsTitle,
            accounts = accounts,
            jarsTitle = jarsTitle,
            jars = jars
        )
    }.stateInViewModelScope(viewModelScope)

    fun selectAccount(account: AccountUiListModel) {
        // TODO()
    }
}

data class SelectAccountUiState(
    val title: String,
    val backButtonVisible: Boolean,
    val accountsTitle: String,
    val accounts: List<AccountUiListModel>,
    val jarsTitle: String,
    val jars: List<AccountUiListModel.Jar>
)
