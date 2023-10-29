package com.chummer.domain.di

import com.chummer.domain.GetAllClientAccountsUseCase
import com.chummer.finance.db.di.DbModule
import com.chummer.finance.db.mono.account.GetAccountsUseCase
import com.chummer.finance.db.mono.account.UpsertAccountsUseCase
import com.chummer.finance.db.mono.jar.GetJarsUseCase
import com.chummer.finance.db.mono.jar.UpsertJarsUseCase
import com.chummer.finance.network.di.NetworkModule
import com.chummer.finance.network.monobank.account.GetPersonalInfoUseCase
import com.chummer.preferences.di.PreferencesModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.chummer.domain.mono.FetchPersonalInfoUseCase as FetchMonoPersonalInfoUseCase

@InstallIn(SingletonComponent::class)
@Module(
    includes = [
        DbModule::class,
        NetworkModule::class,
        PreferencesModule::class
    ]
)
object DomainModule {
    @Provides
    fun providesGetAllClientAccountsUseCase(
        getAccountsUseCase: GetAccountsUseCase,
        getJarsUseCase: GetJarsUseCase
    ) = GetAllClientAccountsUseCase(getAccountsUseCase, getJarsUseCase)

    @Provides
    fun provideFetchMonoUseCase(
        getPersonalInfoUseCase: GetPersonalInfoUseCase,
        upsertAccountsUseCase: UpsertAccountsUseCase,
        upsertJarsUseCase: UpsertJarsUseCase
    ) = FetchMonoPersonalInfoUseCase(
        getPersonalInfoUseCase,
        upsertAccountsUseCase,
        upsertJarsUseCase
    )
}