package com.chummer.finance.db.mono.transaction

import com.chummer.finance.ChummerFinanceDatabase
import com.chummer.finance.db.di.DbComponentsModule
import com.chummer.finance.db.mono.transaction.getTransaction.GetTransactionFlowUseCase
import com.chummer.finance.db.mono.transaction.getTransactions.GetTransactionsFlowUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module(includes = [DbComponentsModule::class])
object TransactionUseCaseModule {
    @Provides
    fun provideGetTransactionUseCase(
        db: ChummerFinanceDatabase
    ) = GetTransactionFlowUseCase(db.operationQueries)

    @Provides
    fun provideGetTransactionsUseCase(
        db: ChummerFinanceDatabase
    ) = GetTransactionsFlowUseCase(db.operationQueries)

    @Provides
    fun provideUpsertTransactionsUseCase(
        db: ChummerFinanceDatabase
    ) = UpsertTransactionsUseCase(db.operationQueries)
}
