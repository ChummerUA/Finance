package com.chummer.finance.db.mono.jar

import com.chummer.finance.ChummerFinanceDatabase
import com.chummer.finance.db.di.DbComponentsModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module(includes = [DbComponentsModule::class])
object JarUseCasesModule {
    @Provides
    fun provideUpsertJarsUseCase(db: ChummerFinanceDatabase) = UpsertJarsUseCase(db.jarQueries)

    @Provides
    fun provideGetJarsUseCase(db: ChummerFinanceDatabase) = GetJarsFlowUseCase(db.jarQueries)

    @Provides
    fun provideDeleteJarsThatAreNotInListUseCase(
        db: ChummerFinanceDatabase
    ) = DeleteJarsThatAreNotInListUseCase(db.jarQueries)
}