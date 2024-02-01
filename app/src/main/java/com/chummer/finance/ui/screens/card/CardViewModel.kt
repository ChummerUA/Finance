package com.chummer.finance.ui.screens.card

import android.app.Application
import android.content.Context
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
import com.chummer.finance.ui.account.DayWithTransactions
import com.chummer.finance.ui.account.toUiModel
import com.chummer.finance.ui.transaction.TransactionUiListModel
import com.chummer.finance.utils.PagingDirection
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CardViewModel @Inject constructor(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
    getAccountFlow: GetAccountFlowUseCase,
    getTransactionsFlow: GetTransactionsFlowUseCase,
    private val getLastTransactionsFetchTime: GetLastTransactionsFetchTimeUseCase,
) : AndroidViewModel(application) {

    private val workerManager = WorkManager.getInstance(application.applicationContext)

    private val accountId: String = savedStateHandle["id"] ?: error("missing account id")

    private val dateTimeArgument =
        savedStateHandle.getStateFlow(DATE_TIME_ARGUMENT_KEY, LocalDateTime.now().toUnixSecond())
    private val pages = savedStateHandle.getStateFlow(PAGES_COUNT_KEY, 1L)
    private val isBackDirection = savedStateHandle.getStateFlow(IS_BACK_DIRECTION_KEY, false)

    private val argument = combine(
        dateTimeArgument,
        pages,
        isBackDirection
    ) { dateTimeArgument, pages, isBackDirection ->
        GetTransactionsArgument(
            accountId = accountId,
            time = dateTimeArgument,
            pageSize = PAGE_SIZE,
            pages = pages,
            shiftOnePageBack = isBackDirection
        )
    }

    private val accountFlow = getAccountFlow(accountId).map {
        it.toUiModel(application)
    }

    private val transactions = argument.flatMapLatest { getTransactionsFlow(it) }
    private val firstTransactionOnSecondPage = transactions.map {
        it.drop(PAGE_SIZE.toInt()).first()
    }

    private val daysWithTransactionsFlow: Flow<ImmutableList<DayWithTransactions>> =
        transactions.map { it.groupToTransactionsInDays(application) }

    val state = combine(accountFlow, daysWithTransactionsFlow) { account, days ->
        CardUiState(
            account,
            days
        )
    }.stateInViewModelScope(viewModelScope)

    init {
        scheduleFetch()
        viewModelScope.launch {
            argument.collect {
                Log.d(TAG, "Loading transactions. From ${it.time}, ${it.pages} pages")
            }
        }
    }

    private fun scheduleFetch() = viewModelScope.launch {
        val lastFetchTime = getLastTransactionsFetchTime(accountId)
        Log.d(TAG, "Scheduling fetch. Last fetch time: $lastFetchTime")
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

    fun updatePages(direction: PagingDirection) = viewModelScope.launch {
        if (direction is PagingDirection.Forward) {
            pages.value.let {
                if (it < MAX_PAGES)
                    savedStateHandle[PAGES_COUNT_KEY] = it + 1
                else
                    savedStateHandle[DATE_TIME_ARGUMENT_KEY] =
                        firstTransactionOnSecondPage.first().time
            }
        } else savedStateHandle[IS_BACK_DIRECTION_KEY] = pages.value > MAX_PAGES
    }
}

private fun List<ListTransactionItem>.groupToTransactionsInDays(context: Context) =
    groupBy { it.time.toLocalDateTime().toLocalDate() }
        .map { (date, items) ->
            DayWithTransactions(
                date,
                date.toDateString(context),
                items.toUiTransactions()
            )
        }
        .sortedByDescending { it.date.toUnixSecond() }
        .toImmutableList()

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

private const val TAG = "CardViewModel"

private const val PAGE_SIZE = 50L
private const val MAX_PAGES = 6L

private const val DATE_TIME_ARGUMENT_KEY = "date_time"
private const val PAGES_COUNT_KEY = "pages_count"
private const val IS_BACK_DIRECTION_KEY = "is_back_direction"
