package com.chummer.finance.ui.screens.account

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.chummer.finance.db.mono.account.getAccountFlow.GetAccountFlowUseCase
import com.chummer.finance.db.mono.transaction.getTransaction.GetTransactionsArgument
import com.chummer.finance.db.mono.transaction.getTransactions.GetTransactionsFlowUseCase
import com.chummer.finance.db.mono.transaction.getTransactions.ListTransactionItem
import com.chummer.finance.ui.screens.transaction.TransactionUiListModel
import com.chummer.finance.utils.getFormattedAmountAndCurrency
import com.chummer.finance.utils.stateInViewModelScope
import com.chummer.finance.utils.toDateString
import com.chummer.finance.utils.toLocalDateTime
import com.chummer.finance.utils.toTimeString
import com.chummer.finance.utils.toUnixSecond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
    getAccountFlow: GetAccountFlowUseCase,
    getTransactionsFlow: GetTransactionsFlowUseCase
) : AndroidViewModel(application) {
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
    Log.d("AccountViewModel", "Mapping ${it.time} to ${it.time.toLocalDateTime()}")
    TransactionUiListModel(
        id = it.id,
        name = it.description,
        time = it.time.toLocalDateTime().toTimeString(),
        amount = getFormattedAmountAndCurrency(it.operationAmount, it.currencyCode),
        income = it.operationAmount > 0,
        null
    )
}.toImmutableList()
