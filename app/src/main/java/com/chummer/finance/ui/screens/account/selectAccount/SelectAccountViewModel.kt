package com.chummer.finance.ui.screens.account.selectAccount

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.chummer.domain.GetAccountsAndJarsFlowUseCase
import com.chummer.finance.R
import com.chummer.finance.utils.stateInViewModelScope
import com.chummer.finance.workers.FetchMonoAccountsWorker
import com.chummer.finance.workers.FetchMonoTransactionsWorker
import com.chummer.finance.workers.FetchOperationsType
import com.chummer.models.None
import com.chummer.preferences.mono.lastInfoFetchTime.GetLastMonoAccountsFetchTimeUseCase
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
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@HiltViewModel
class SelectAccountViewModel @Inject constructor(
    private val application: Application,
    private val getLastMonoFetchTime: GetLastMonoAccountsFetchTimeUseCase,
    getAccountsAndJarsFlow: GetAccountsAndJarsFlowUseCase,
//    private val setSelectedAccountUseCase: Any = TODO()
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
        scheduleFetch()
    }

    private fun scheduleFetch() = viewModelScope.launch {
        val now = LocalDateTime.now()
        val lastFetchTime = LocalDateTime.parse(
            getLastMonoFetchTime(),
            DateTimeFormatter.ISO_DATE_TIME
        )

        Log.d(TAG, "Last fetch time: $lastFetchTime")
        val nextAvailableTime = lastFetchTime.plusMinutes(1)
        Log.d(TAG, "Next fetch time: $nextAvailableTime")
        val shouldDelay = lastFetchTime != LocalDateTime.MIN && nextAvailableTime.isAfter(now)
        Log.d(TAG, "Should delay: $shouldDelay")

        val request = OneTimeWorkRequestBuilder<FetchMonoAccountsWorker>()
            .apply {
                if (shouldDelay) {
                    val between = ChronoUnit.SECONDS.between(nextAvailableTime, now)
                    val delay = between.seconds.absoluteValue
                    Log.d(TAG, "Delay in seconds: ${delay.inWholeSeconds}")
                    setInitialDelay(delay.toJavaDuration())
                }
            }
            .build()
        workerManager.enqueueUniqueWork(
            FetchMonoAccountsWorker.name,
            ExistingWorkPolicy.KEEP,
            request
        )
    }

    fun selectAccount(item: AccountUiListModel) {

        val type = when (item) {
            is AccountUiListModel.Jar -> FetchOperationsType.JAR
            else -> FetchOperationsType.ACCOUNT
        }

        val data = Data.Builder().apply {
            putString(FetchMonoTransactionsWorker.ID_KEY, item.id)
            putInt(FetchMonoTransactionsWorker.FETCH_TYPE_KEY, type.value)
        }.build()

        val request = OneTimeWorkRequestBuilder<FetchMonoTransactionsWorker>()
            .setInputData(data)
            .build()

        workerManager.enqueueUniqueWork(
            FetchMonoTransactionsWorker.NAME,
            ExistingWorkPolicy.KEEP,
            request
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
