package com.chummer.finance.db.mono.operation

import com.chummer.finance.ChummerFinanceDatabase
import com.chummer.finance.db.di.DbComponentsModule
import com.chummer.finance.db.mono.operation.getOperation.GetOperationUseCase
import com.chummer.finance.db.mono.operation.getOperations.GetOperationsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module(includes = [DbComponentsModule::class])
object OperationUseCaseModule {
    @Provides
    fun provideGetOperationUseCase(
        db: ChummerFinanceDatabase
    ) = GetOperationUseCase(db.operationQueries)

    @Provides
    fun provideGetOperationsUseCase(
        db: ChummerFinanceDatabase
    ) = GetOperationsUseCase(db.operationQueries)

    @Provides
    fun provideUpsertOperationsUseCase(
        db: ChummerFinanceDatabase
    ) = UpsertOperationsUseCase(db.operationQueries)
}
