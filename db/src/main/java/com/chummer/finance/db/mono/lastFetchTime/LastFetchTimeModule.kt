package com.chummer.finance.db.mono.lastFetchTime

import com.chummer.finance.ChummerFinanceDatabase
import com.chummer.finance.db.di.DbComponentsModule
import com.chummer.finance.db.mono.lastFetchTime.upsertLastFetchTime.UpsertFetchTimeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module(includes = [DbComponentsModule::class])
object LastFetchTimeModule {
    @Provides
    fun provideUpsertFetchTimeUseCase(
        db: ChummerFinanceDatabase
    ): UpsertFetchTimeUseCase = UpsertFetchTimeUseCase(
        db.lastFetchTimeQueries
    )

    @Provides
    fun provideGetLastTransactionsFetchTimeUseCase(
        db: ChummerFinanceDatabase
    ): GetLastTransactionsFetchTimeUseCase = GetLastTransactionsFetchTimeUseCase(
        db.lastFetchTimeQueries
    )
}
