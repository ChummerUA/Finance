package com.chummer.preferences.di

import android.content.Context
import com.chummer.preferences.mono.lastInfoFetchTime.GetLastMonoAccountsFetchTimeUseCase
import com.chummer.preferences.mono.lastInfoFetchTime.SetLastMonoAccountsFetchTimeUseCase
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
}
