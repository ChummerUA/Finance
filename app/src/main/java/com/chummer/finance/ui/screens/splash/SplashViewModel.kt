package com.chummer.finance.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chummer.domain.mono.selectedAccount.GetSelectedAccountUseCase
import com.chummer.models.None
import com.chummer.preferences.mono.selectedAccountType.ACCOUNT_TYPE_CARD
import com.chummer.preferences.mono.selectedAccountType.ACCOUNT_TYPE_JAR
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val getSelectedAccount: GetSelectedAccountUseCase
) : ViewModel() {
    private val navigationChannel = Channel<NavigationEvent>()
    val navigationFlow = navigationChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val selectedAccount = getSelectedAccount(None)
            val event = when (selectedAccount?.accountType) {
                ACCOUNT_TYPE_CARD -> NavigationEvent.Card(selectedAccount.accountId)
                ACCOUNT_TYPE_JAR -> NavigationEvent.Jar(selectedAccount.accountId)
                else -> NavigationEvent.SelectAccount
            }
            navigationChannel.send(event)
        }
    }

    sealed interface NavigationEvent {
        data object SelectAccount : NavigationEvent

        data class Card(val id: String) : NavigationEvent

        data class Jar(val id: String) : NavigationEvent
    }
}
