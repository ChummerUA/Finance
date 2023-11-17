package com.chummer.preferences.di

import android.content.Context
import com.chummer.preferences.mono.lastInfoFetchTime.GetLastMonoAccountsFetchTimeUseCase
import com.chummer.preferences.mono.lastInfoFetchTime.SetLastMonoAccountsFetchTimeUseCase
import com.chummer.preferences.mono.selectedAccount.GetSelectedAccountIdUseCase
import com.chummer.preferences.mono.selectedAccount.SetSelectedAccountIdUseCase
import com.chummer.preferences.mono.selectedAccountType.GetSelectedAccountTypeUseCase
import com.chummer.preferences.mono.selectedAccountType.SetSelectedAccountTypeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {
    @Provides
    fun provideGetLastMonoFetchUseCase(
        @ApplicationContext context: Context
    ) = GetLastMonoAccountsFetchTimeUseCase(context)

    @Provides
    fun provideSetLastMonoFetchUseCase(
        @ApplicationContext context: Context
    ) = SetLastMonoAccountsFetchTimeUseCase(context)

    @Provides
    fun provideGetSelectedAccountIdUseCase(
        @ApplicationContext context: Context
    ): GetSelectedAccountIdUseCase = GetSelectedAccountIdUseCase(
        context
    )

    @Provides
    fun provideSetSelectedAccountIdUseCase(
        @ApplicationContext context: Context
    ): SetSelectedAccountIdUseCase = SetSelectedAccountIdUseCase(
        context
    )

    @Provides
    fun provideGetSelectedAccountTypeUseCase(
        @ApplicationContext context: Context
    ): GetSelectedAccountTypeUseCase = GetSelectedAccountTypeUseCase(
        context
    )

    @Provides
    fun provideSetSelectedAccountTypeUseCase(
        @ApplicationContext context: Context
    ): SetSelectedAccountTypeUseCase = SetSelectedAccountTypeUseCase(
        context
    )
}
