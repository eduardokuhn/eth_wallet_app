package com.example.ethwalletapp.shared.hilt

import com.example.ethwalletapp.data.data_sources.AppDatabase
import com.example.ethwalletapp.data.repositories.AccountRepositoryImpl
import com.example.ethwalletapp.data.repositories.IAccountRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
  @Provides
  fun provideAccountRepositoryImpl(appDatabase: AppDatabase) : IAccountRepository {
    return AccountRepositoryImpl(appDatabase.accountDao())
  }
}