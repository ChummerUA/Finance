package com.chummer.finance.di

import com.chummer.domain.di.DomainModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [DomainModule::class])
@InstallIn(SingletonComponent::class)
object AppModule {
}
