package com.chummer.finance.ui.screens.selectAccount

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.chummer.domain.mono.GetAccountsAndJarsFlowUseCase
import com.chummer.domain.mono.selectedAccount.SelectedAccount
import com.chummer.domain.mono.selectedAccount.SetSelectedAccountUseCase
import com.chummer.finance.R
import com.chummer.finance.ui.account.AccountUiListModel
import com.chummer.finance.ui.account.toUiModel
import com.chummer.finance.utils.scheduleFetchWorker
import com.chummer.finance.utils.stateInViewModelScope
import com.chummer.finance.workers.FetchMonoAccountsWorker
import com.chummer.models.None
import com.chummer.preferences.mono.lastInfoFetchTime.GetLastMonoAccountsFetchTimeUseCase
import com.chummer.preferences.mono.selectedAccountType.ACCOUNT_TYPE_CARD
import com.chummer.preferences.mono.selectedAccountType.ACCOUNT_TYPE_JAR
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

@HiltViewModel
class SelectAccountViewModel @Inject constructor(
    private val application: Application,
    private val getLastMonoFetchTime: GetLastMonoAccountsFetchTimeUseCase,
    private val setSelectedAccount: SetSelectedAccountUseCase,
    getAccountsAndJarsFlow: GetAccountsAndJarsFlowUseCase
) : AndroidViewModel(application) {

    private val workerManager = WorkManager.getInstance(application.applicationContext)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val allAccountsFlow = getAccountsAndJarsFlow(None).mapLatest { list ->
        list.map { it.toUiModel(application.applicationContext) }
    }
    private val title = application.getString(R.string.select_account_title)
    private val accountsTitle = application.getString(R.string.select_account_cards)
    private val fopTitle = application.getString(R.string.select_account_fop)
    private val jarsTitle: String = application.getString(R.string.select_account_jars)

    private val accountsFlow =
        allAccountsFlow.mapAccountsToGroup<AccountUiListModel.Card>(accountsTitle)
    private val fopAccountsFlow =
        allAccountsFlow.mapAccountsToGroup<AccountUiListModel.FOP>(fopTitle)
    private val jarsFlow = allAccountsFlow.mapAccountsToGroup<AccountUiListModel.Jar>(jarsTitle)

    val state = combine(
        accountsFlow,
        fopAccountsFlow,
        jarsFlow
    ) { accounts, fopAccounts, jars ->
        SelectAccountUiState(
            title = title,
            backButtonVisible = false,
            cardGroup = accounts,
            fopGroup = fopAccounts,
            jarGroup = jars
        )
    }.stateInViewModelScope(viewModelScope)

    init {
        fetchAccounts()
    }

    private fun fetchAccounts() = viewModelScope.launch {
        val lastFetchTime = LocalDateTime.parse(
            getLastMonoFetchTime(),
            DateTimeFormatter.ISO_DATE_TIME
        )
        workerManager.scheduleFetchWorker<FetchMonoAccountsWorker>(
            FetchMonoAccountsWorker.name,
            null,
            1.minutes,
            lastFetchTime
        )
    }

    fun selectAccount(item: AccountUiListModel) = viewModelScope.launch {
        val type = when (item) {
            is AccountUiListModel.Jar -> ACCOUNT_TYPE_JAR
            else -> ACCOUNT_TYPE_CARD
        }

        setSelectedAccount(
            SelectedAccount(item.id, type)
        )
    }

    private companion object {
        const val TAG = "SelectAccountViewModel"
    }
}

data class SelectAccountUiState(
    val title: String,
    val backButtonVisible: Boolean,
    val cardGroup: AccountsGroup<AccountUiListModel.Card>,
    val fopGroup: AccountsGroup<AccountUiListModel.FOP>,
    val jarGroup: AccountsGroup<AccountUiListModel.Jar>
)

data class AccountsGroup<T : AccountUiListModel>(
    val title: String,
    val items: ImmutableList<T>,
    val groupShown: Boolean = items.any()
)

private inline fun <reified T : AccountUiListModel> Flow<List<AccountUiListModel>>.mapAccountsToGroup(
    title: String
) =
    map {
        AccountsGroup(
            title,
            it.filterIsInstance<T>().toImmutableList()
        )
    }
