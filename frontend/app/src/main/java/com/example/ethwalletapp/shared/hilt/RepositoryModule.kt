package com.example.ethwalletapp.shared.hilt

import android.content.SharedPreferences
import com.example.ethwalletapp.data.data_sources.IEthereumApi
import com.example.ethwalletapp.data.data_sources.LocalAppDatabase
import com.example.ethwalletapp.data.repositories.AccountRepositoryImpl
import com.example.ethwalletapp.data.repositories.BalanceRepositoryImpl
import com.example.ethwalletapp.data.repositories.IAccountRepository
import com.example.ethwalletapp.data.repositories.IBalanceRepository
import com.example.ethwalletapp.data.services.IConnectivityService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
  @Singleton
  @Provides
  fun provideAccountRepositoryImpl(localAppDatabase: LocalAppDatabase) : IAccountRepository {
    return AccountRepositoryImpl(localAppDatabase.accountDao())
  }

  @Singleton
  @Provides
  fun provideBalanceRepositoryImpl(
    ethereumApi: IEthereumApi,
    localAppDatabase: LocalAppDatabase,
    sharedPreferences: SharedPreferences,
    connectivityService: IConnectivityService
  ): IBalanceRepository {
    return BalanceRepositoryImpl(ethereumApi, localAppDatabase.balanceDao(), sharedPreferences, connectivityService)
  }
}