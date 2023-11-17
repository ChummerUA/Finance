package com.chummer.domain.di

import com.chummer.domain.mono.GetAccountsAndJarsFlowUseCase
import com.chummer.domain.mono.fetchTransactions.FetchMonoTransactionsUseCase
import com.chummer.domain.mono.selectedAccount.GetSelectedAccountUseCase
import com.chummer.domain.mono.selectedAccount.SetSelectedAccountUseCase
import com.chummer.finance.db.di.DbUseCasesModule
import com.chummer.finance.db.mono.account.DeleteAccountsThatAreNotInListUseCase
import com.chummer.finance.db.mono.account.GetAccountsFlowUseCase
import com.chummer.finance.db.mono.account.UpsertAccountsUseCase
import com.chummer.finance.db.mono.jar.DeleteJarsThatAreNotInListUseCase
import com.chummer.finance.db.mono.jar.GetJarsFlowUseCase
import com.chummer.finance.db.mono.jar.UpsertJarsUseCase
import com.chummer.finance.db.mono.transaction.UpsertTransactionsUseCase
import com.chummer.finance.network.di.NetworkModule
import com.chummer.finance.network.monobank.account.GetPersonalInfoUseCase
import com.chummer.finance.network.monobank.transactions.GetTransactionsUseCase
import com.chummer.preferences.di.PreferencesModule
import com.chummer.preferences.mono.selectedAccount.GetSelectedAccountIdUseCase
import com.chummer.preferences.mono.selectedAccount.SetSelectedAccountIdUseCase
import com.chummer.preferences.mono.selectedAccountType.GetSelectedAccountTypeUseCase
import com.chummer.preferences.mono.selectedAccountType.SetSelectedAccountTypeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.chummer.domain.mono.fetchAccounts.FetchMonoAccountsUseCase as FetchMonoPersonalInfoUseCase

@InstallIn(SingletonComponent::class)
@Module(
    includes = [
        DbUseCasesModule::class,
        NetworkModule::class,
        PreferencesModule::class
    ]
)
object DomainModule {
    @Provides
    fun providesGetAllClientAccountsUseCase(
        getAccountsFlowUseCase: GetAccountsFlowUseCase,
        getJarsFlowUseCase: GetJarsFlowUseCase
    ) = GetAccountsAndJarsFlowUseCase(getAccountsFlowUseCase, getJarsFlowUseCase)

    @Provides
    fun provideFetchMonoInfoUseCase(
        getPersonalInfoUseCase: GetPersonalInfoUseCase,
        upsertAccountsUseCase: UpsertAccountsUseCase,
        upsertJarsUseCase: UpsertJarsUseCase,
        deleteAccountsThatAreNotInListUseCase: DeleteAccountsThatAreNotInListUseCase,
        deleteJarsThatAreNotInListUseCase: DeleteJarsThatAreNotInListUseCase
    ) = FetchMonoPersonalInfoUseCase(
        getPersonalInfoUseCase,
        upsertAccountsUseCase,
        upsertJarsUseCase,
        deleteAccountsThatAreNotInListUseCase,
        deleteJarsThatAreNotInListUseCase
    )

    @Provides
    fun provideFetchMonoTransactionsUseCase(
        getTransactionsUseCase: GetTransactionsUseCase,
        upsertTransactionsUseCase: UpsertTransactionsUseCase
    ): FetchMonoTransactionsUseCase = FetchMonoTransactionsUseCase(
        getTransactionsUseCase,
        upsertTransactionsUseCase
    )

    @Provides
    fun provideGetSelectedAccountUseCase(
        getSelectedAccountIdUseCase: GetSelectedAccountIdUseCase,
        getSelectedAccountTypeUseCase: GetSelectedAccountTypeUseCase
    ): GetSelectedAccountUseCase = GetSelectedAccountUseCase(
        getSelectedAccountIdUseCase,
        getSelectedAccountTypeUseCase
    )

    @Provides
    fun provideSetSelectedAccountUseCase(
        setSelectedAccountIdUseCase: SetSelectedAccountIdUseCase,
        setSelectedAccountTypeUseCase: SetSelectedAccountTypeUseCase
    ): SetSelectedAccountUseCase = SetSelectedAccountUseCase(
        setSelectedAccountIdUseCase,
        setSelectedAccountTypeUseCase
    )
}