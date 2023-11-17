package com.chummer.finance.ui.screens.account

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.WorkManager
import com.chummer.finance.db.mono.account.getAccountFlow.GetAccountFlowUseCase
import com.chummer.finance.db.mono.lastFetchTime.GetLastTransactionsFetchTimeUseCase
import com.chummer.finance.db.mono.transaction.getTransaction.GetTransactionsArgument
import com.chummer.finance.db.mono.transaction.getTransactions.GetTransactionsFlowUseCase
import com.chummer.finance.db.mono.transaction.getTransactions.ListTransactionItem
import com.chummer.finance.ui.account.AccountUiState
import com.chummer.finance.ui.account.DayWithTransactions
import com.chummer.finance.ui.account.toUiModel
import com.chummer.finance.ui.transaction.TransactionUiListModel
import com.chummer.finance.utils.getFormattedAmountAndCurrency
import com.chummer.finance.utils.scheduleFetchWorker
import com.chummer.finance.utils.stateInViewModelScope
import com.chummer.finance.utils.toDateString
import com.chummer.finance.utils.toTimeString
import com.chummer.finance.workers.FetchMonoTransactionsWorker
import com.chummer.models.mapping.toLocalDateTime
import com.chummer.models.mapping.toUnixSecond
import com.chummer.preferences.mono.selectedAccountType.ACCOUNT_TYPE_CARD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

@HiltViewModel
class AccountViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
    getAccountFlow: GetAccountFlowUseCase,
    getTransactionsFlow: GetTransactionsFlowUseCase,
    private val getLastTransactionsFetchTime: GetLastTransactionsFetchTimeUseCase,
) : AndroidViewModel(application) {
    private val workerManager = WorkManager.getInstance(application.applicationContext)

    private val accountId: String =
        savedStateHandle.get<String>("id") ?: error("missing account id")

    private val argument = GetTransactionsArgument(
        accountId,
        null,
        LocalDateTime.now().toUnixSecond(),
        50
    )

    private val daysWithTransactionsFlow: Flow<ImmutableList<DayWithTransactions>> =
        getTransactionsFlow(
            argument
        ).map { it.groupToTransactionsInDays() }

    private val accountFlow = getAccountFlow(accountId).map {
        it.toUiModel(application)
    }

    val state = combine(accountFlow, daysWithTransactionsFlow) { account, days ->
        AccountUiState(
            account,
            days
        )
    }.stateInViewModelScope(viewModelScope)

    init {
        scheduleFetch()
    }

    private fun scheduleFetch() = viewModelScope.launch {
        val lastFetchTime = getLastTransactionsFetchTime(accountId)
        Log.d("AccountViewModel", "Scheduling fetch. Last fetch time: $lastFetchTime")
        val data = Data.Builder().apply {
            putString(FetchMonoTransactionsWorker.ID_KEY, accountId)
            putInt(FetchMonoTransactionsWorker.FETCH_TYPE_KEY, ACCOUNT_TYPE_CARD)
            lastFetchTime?.let {
                putLong(FetchMonoTransactionsWorker.LAST_FETCH_TIME_KEY, it.toUnixSecond())
            }
        }.build()

        workerManager.scheduleFetchWorker<FetchMonoTransactionsWorker>(
            FetchMonoTransactionsWorker.NAME,
            data,
            1.minutes,
            lastFetchTime
        )
    }
}

private fun List<ListTransactionItem>.groupToTransactionsInDays() =
    groupBy { it.time.toLocalDateTime().toLocalDate().toDateString() }
        .map { (date, items) ->
            DayWithTransactions(
                date,
                items.toUiTransactions()
            )
        }.toImmutableList()

private fun List<ListTransactionItem>.toUiTransactions() = map {
    TransactionUiListModel(
        id = it.id,
        name = it.description,
        time = it.time.toLocalDateTime().toTimeString(),
        amount = getFormattedAmountAndCurrency(it.operationAmount, it.currencyCode),
        income = it.operationAmount > 0,
        null
    )
}.toImmutableList()

//1696203322/1697713404
//1697737666/1700264194