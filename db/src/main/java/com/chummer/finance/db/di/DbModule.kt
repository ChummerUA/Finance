package com.chummer.finance.db.di

import android.app.Application
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.chummer.finance.ChummerFinanceDatabase
import com.chummer.finance.db.mono.account.GetAccountsUseCase
import com.chummer.finance.db.mono.account.UpsertAccountsUseCase
import com.chummer.finance.db.mono.jar.GetJarsUseCase
import com.chummer.finance.db.mono.jar.UpsertJarsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mono.Account
import mono.Jar
import mono.Operation
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DbModule {
    @Singleton
    @Provides
    fun provideDataBase(
        sqlDriver: SqlDriver,
        jarAdapter: Jar.Adapter,
        operationAdapter: Operation.Adapter,
        accountAdapter: Account.Adapter
    ): ChummerFinanceDatabase = ChummerFinanceDatabase(
        driver = sqlDriver,
        jarAdapter = jarAdapter,
        operationAdapter = operationAdapter,
        accountAdapter = accountAdapter
    )

    @Provides
    fun provideSqlDriver(
        app: Application
    ): SqlDriver = AndroidSqliteDriver(
        ChummerFinanceDatabase.Schema,
        app.applicationContext,
        "ChummerFinanceDatabase"
    )

    @Provides
    fun provideJarAdapter(): Jar.Adapter = Jar.Adapter(IntColumnAdapter)

    @Provides
    fun provideOperationAdapter(): Operation.Adapter = Operation.Adapter(
        mccAdapter = IntColumnAdapter,
        original_mccAdapter = IntColumnAdapter,
        currency_codeAdapter = IntColumnAdapter
    )

    @Provides
    fun provideAccountAdapter(): Account.Adapter = Account.Adapter(
        currency_codeAdapter = IntColumnAdapter
    )

    @Provides
    fun provideUpsertAccountsUseCase(db: ChummerFinanceDatabase) =
        UpsertAccountsUseCase(db.accountQueries)

    @Provides
    fun provideUpsertJarsUseCase(db: ChummerFinanceDatabase) = UpsertJarsUseCase(db.jarQueries)

    @Provides
    fun provideGetAccountsUseCase(db: ChummerFinanceDatabase) =
        GetAccountsUseCase(db.accountQueries)

    @Provides
    fun provideGetJarsUseCase(db: ChummerFinanceDatabase) = GetJarsUseCase(db.jarQueries)
}