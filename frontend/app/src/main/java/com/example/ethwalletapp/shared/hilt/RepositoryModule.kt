package com.example.ethwalletapp.shared.hilt

import com.example.ethwalletapp.data.data_sources.LocalAppDatabase
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
  fun provideAccountRepositoryImpl(localAppDatabase: LocalAppDatabase) : IAccountRepository {
    return AccountRepositoryImpl(localAppDatabase.accountDao())
  }
}