package com.example.ethwalletapp.shared.hilt

import android.content.SharedPreferences
import com.example.ethwalletapp.data.data_sources.IEthereumApi
import com.example.ethwalletapp.data.data_sources.LocalAppDatabase
import com.example.ethwalletapp.data.repositories.*
import com.example.ethwalletapp.data.services.IConnectivityService
import com.example.ethwalletapp.data.services.IEthereumNetworkService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
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
    ethereumNetworkService: IEthereumNetworkService,
    ethereumApi: IEthereumApi,
    localAppDatabase: LocalAppDatabase,
    @Named("sharedPreferences")
    sharedPreferences: SharedPreferences,
    connectivityService: IConnectivityService
  ): IBalanceRepository {
    return BalanceRepositoryImpl(
      ethereumNetworkService = ethereumNetworkService,
      ethereumApi = ethereumApi,
      balanceDao = localAppDatabase.balanceDao(),
      sharedPreferences = sharedPreferences,
      connectivityService = connectivityService
    )
  }

  @Singleton
  @Provides
  fun provideTransactionRepositoryImpl(
    ethereumApi: IEthereumApi,
    ethereumNetworkService: IEthereumNetworkService,
    localAppDatabase: LocalAppDatabase,
    connectivityService: IConnectivityService
  ): ITransactionRepository {
    return TransactionRepositoryImpl(
      ethereumApi = ethereumApi,
      ethereumNetworkService = ethereumNetworkService,
      transactionDao = localAppDatabase.transactionDao(),
      connectivityService = connectivityService
    )
  }
}