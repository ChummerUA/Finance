package com.chummer.finance.ui.screens.card

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.WorkManager
import com.chummer.domain.Category
import com.chummer.finance.db.mono.account.getAccountFlow.GetAccountFlowUseCase
import com.chummer.finance.db.mono.lastFetchTime.GetLastTransactionsFetchTimeUseCase
import com.chummer.finance.db.mono.transaction.getTransaction.GetTransactionsArgument
import com.chummer.finance.db.mono.transaction.getTransaction.GetTransactionsPagingConfig
import com.chummer.finance.db.mono.transaction.getTransactions.GetTransactionsFlowUseCase
import com.chummer.finance.db.mono.transaction.getTransactions.ListTransactionItem
import com.chummer.finance.ui.account.DayWithTransactions
import com.chummer.finance.ui.account.toUiModel
import com.chummer.finance.ui.selectedOrDefault
import com.chummer.finance.ui.transaction.SearchBarState
import com.chummer.finance.ui.transaction.TransactionUiListModel
import com.chummer.finance.ui.transaction.getContentDescription
import com.chummer.finance.utils.PagingDirection
import com.chummer.finance.utils.getFormattedAmountAndCurrency
import com.chummer.finance.utils.scheduleFetchWorker
import com.chummer.finance.utils.stateInViewModelScope
import com.chummer.finance.utils.toDateString
import com.chummer.finance.utils.toTimeString
import com.chummer.finance.workers.FetchMonoTransactionsWorker
import com.chummer.models.mapping.toLocalDate
import com.chummer.models.mapping.toLocalDateTime
import com.chummer.models.mapping.toUnixSecond
import com.chummer.preferences.mono.selectedAccountType.ACCOUNT_TYPE_CARD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
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
    private val pagingConfig = combine(
        dateTimeArgument,
        pages,
        isBackDirection
    ) { dateTime, pages, isBack ->
        GetTransactionsPagingConfig(
            time = dateTime,
            pageSize = PAGE_SIZE,
            pages = pages,
            shiftOnePageBack = isBack
        )
    }

    private val range = combine(
        savedStateHandle.getStateFlow<Long?>(FROM_KEY, null),
        savedStateHandle.getStateFlow<Long?>(TO_KEY, null)
    ) { from, to ->
        if (from != null && to != null) from.toLocalDate() to to.toLocalDate()
        else null
    }

    private val selectedCategoryIds = savedStateHandle.getStateFlow("", listOf<Category>())

    private val search = savedStateHandle.getStateFlow(SEARCH_KEY, "")
    private val searchBarMode: StateFlow<SearchBarMode> =
        savedStateHandle.getStateFlow(SEARCH_BAR_MODE_KEY, SearchBarMode.Default)
    private val searchBar = combine(searchBarMode, search, range) { mode, search, range ->
        when (mode) {
            SearchBarMode.Default -> SearchBarState.Default
            else -> SearchBarState.Expanded(
                search,
                categoriesIconState = (mode == SearchBarMode.Categories).selectedOrDefault(),
                calendarIconState = (mode == SearchBarMode.Calendar).selectedOrDefault(),
                range = range
            )
        }
    }

    private val argument = combine(
        pagingConfig,
        search,
        range,
        selectedCategoryIds
    ) { pagingConfig, search, range, selectedCategoryIds ->
        val ids = selectedCategoryIds.map { it.id }
        GetTransactionsArgument(
            accountId = accountId,
            pagingConfig = pagingConfig,
            search = search,
            range = range,
            categoryIds = ids.ifEmpty { Category.entries.map { it.id } }
        )
    }

    private val account = getAccountFlow(accountId).map { it.toUiModel(application) }

    private val transactions = argument.flatMapLatest { getTransactionsFlow(it) }
    private val firstTransactionOnSecondPage = transactions.map {
        it.drop(PAGE_SIZE.toInt()).first()
    }

    private val daysWithTransactionsFlow: Flow<ImmutableList<DayWithTransactions>> =
        transactions.map { it.groupToTransactionsInDays(application) }

    val state = combine(
        account,
        searchBar,
        daysWithTransactionsFlow
    ) { account, searchBar, days ->
        CardUiState(
            account,
            searchBar,
            days
        )
    }.stateInViewModelScope(viewModelScope)

    init {
        scheduleFetch()
        viewModelScope.launch {
            pagingConfig.collect {
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

    fun activateSearch() {
        // TODO replace with regular fields
        savedStateHandle[SEARCH_BAR_MODE_KEY] = SearchBarMode.Search
    }

    fun updateSearchText(newSearch: String) {
        savedStateHandle[SEARCH_KEY] = newSearch
    }

    fun updateSelectedDates(dates: Pair<LocalDate, LocalDate>?) {
        savedStateHandle[FROM_KEY] = dates?.first?.toUnixSecond()
        savedStateHandle[TO_KEY] = dates?.second?.toUnixSecond()
        savedStateHandle[SEARCH_BAR_MODE_KEY] = SearchBarMode.Default
    }

    fun activateCategoriesSelection() {
        savedStateHandle[SEARCH_BAR_MODE_KEY] = SearchBarMode.Categories
    }

    fun selectCategories() {
        savedStateHandle[SEARCH_BAR_MODE_KEY] = SearchBarMode.Default
    }

    fun activateCalendarSelection() {
        savedStateHandle[SEARCH_BAR_MODE_KEY] = SearchBarMode.Calendar
    }

    fun cancelSearch() {
        savedStateHandle[SEARCH_KEY] = ""
        savedStateHandle[SEARCH_BAR_MODE_KEY] = SearchBarMode.Default
    }
}

private fun List<ListTransactionItem>.groupToTransactionsInDays(context: Context) =
    groupBy { it.time.toLocalDateTime().toLocalDate() }
        .map { (date, items) ->
            DayWithTransactions(
                date,
                date.toDateString(context),
                items.toUiTransactions(context)
            )
        }
        .sortedByDescending { it.date.toUnixSecond() }
        .toImmutableList()

private fun List<ListTransactionItem>.toUiTransactions(context: Context) = map {
    TransactionUiListModel(
        id = it.id,
        name = it.description,
        time = it.time.toLocalDateTime().toTimeString(),
        amount = getFormattedAmountAndCurrency(it.operationAmount, it.currencyCode),
        income = it.isIncome,
        icon = null,
        accessibilityText = it.getContentDescription(context)
    )
}.toImmutableList()

private const val TAG = "CardViewModel"

private const val PAGE_SIZE = 50L
private const val MAX_PAGES = 6L

private const val DATE_TIME_ARGUMENT_KEY = "date_time"
private const val PAGES_COUNT_KEY = "pages_count"
private const val IS_BACK_DIRECTION_KEY = "is_back_direction"

private const val SEARCH_KEY = "search"
private const val SEARCH_BAR_MODE_KEY = "search_bar_mode"

private const val FROM_KEY = "from"
private const val TO_KEY = "to"

private enum class SearchBarMode {
    Default,
    Search,
    Categories,
    Calendar
}
