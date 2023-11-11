package com.chummer.finance.network.di

import com.chummer.finance.network.monobank.account.GetPersonalInfoUseCase
import com.chummer.finance.network.monobank.provideMonoClient
import com.chummer.finance.network.monobank.transactions.GetTransactionsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient =
        provideMonoClient("PLACE TOKEN HERE")

    @Provides
    fun provideGetPersonalInfoUseCase(client: HttpClient) = GetPersonalInfoUseCase(client)

    @Provides
    fun provideGetTransactionsUseCase(client: HttpClient) = GetTransactionsUseCase(client)
}
