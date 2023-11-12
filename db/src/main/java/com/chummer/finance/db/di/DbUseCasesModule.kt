package com.chummer.finance.db.di

import com.chummer.finance.db.mono.account.AccountUseCasesModule
import com.chummer.finance.db.mono.jar.JarUseCasesModule
import com.chummer.finance.db.mono.transaction.TransactionUseCaseModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module(
    includes = [
        AccountUseCasesModule::class,
        JarUseCasesModule::class,
        TransactionUseCaseModule::class
    ]
)
object DbUseCasesModule