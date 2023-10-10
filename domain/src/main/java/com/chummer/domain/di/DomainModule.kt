package com.chummer.domain.di

import com.chummer.domain.GetAllClientAccountsUseCase
import com.chummer.finance.ChummerFinanceDatabase
import com.chummer.finance.db.di.DbModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module(includes = [DbModule::class])
object DomainModule {
    @Provides
    fun providesGetAllClientAccountsUseCase(
        db: ChummerFinanceDatabase
    ) = GetAllClientAccountsUseCase(db)
}