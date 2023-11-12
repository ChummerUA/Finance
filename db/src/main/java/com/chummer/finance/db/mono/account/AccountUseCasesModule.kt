package com.chummer.finance.db.mono.account

import com.chummer.finance.ChummerFinanceDatabase
import com.chummer.finance.db.di.DbComponentsModule
import com.chummer.finance.db.mono.account.getAccountFlow.GetAccountFlowUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module(includes = [DbComponentsModule::class])
object AccountUseCasesModule {
    @Provides
    fun provideUpsertAccountsUseCase(db: ChummerFinanceDatabase) =
        UpsertAccountsUseCase(db.accountQueries)


    @Provides
    fun provideGetAccountsFlowUseCase(db: ChummerFinanceDatabase) =
        GetAccountsFlowUseCase(db.accountQueries)

    @Provides
    fun provideGetAccountFlowUseCase(db: ChummerFinanceDatabase) =
        GetAccountFlowUseCase(db.accountQueries)


    @Provides
    fun provideDeleteAccountsThatAreNotInListUseCase(
        db: ChummerFinanceDatabase
    ) = DeleteAccountsThatAreNotInListUseCase(db.accountQueries)
}