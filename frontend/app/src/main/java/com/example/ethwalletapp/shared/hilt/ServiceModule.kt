package com.example.ethwalletapp.shared.hilt

import android.content.Context
import android.content.SharedPreferences
import com.example.ethwalletapp.data.repositories.IAccountRepository
import com.example.ethwalletapp.data.repositories.ITransactionRepository
import com.example.ethwalletapp.data.services.*
import com.example.ethwalletapp.shared.network.HostSelectionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.kethereum.keystore.api.KeyStore
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
  @Provides
  fun provideAccountServiceImpl(
    keyStore: KeyStore,
    @Named("encryptedSharedPreferences")
    encryptedSharedPreferences: SharedPreferences,
    accountRepository: IAccountRepository
  ) : IAccountService {
    return AccountServiceImpl(keyStore, encryptedSharedPreferences, accountRepository)
  }

  @Provides
  fun provideConnectivityServiceImpl(@ApplicationContext context: Context): IConnectivityService {
    return ConnectivityServiceImpl(context)
  }

  @Provides
  fun provideEtherscanNetworkServiceImpl(hostSelectionInterceptor: HostSelectionInterceptor): IEthereumNetworkService {
    return EtherscanNetworkServiceImpl(hostSelectionInterceptor)
  }

  @Provides
  fun provideTransactionServiceImpl(
    keyStore: KeyStore,
    @Named("encryptedSharedPreferences")
    encryptedSharedPreferences: SharedPreferences,
    ethereumNetworkService: IEthereumNetworkService,
    transactionRepository: ITransactionRepository
  ): ITransactionService {
    return TransactionServiceImpl(
      keyStore = keyStore,
      encryptedSharedPreferences = encryptedSharedPreferences,
      ethereumNetworkService = ethereumNetworkService,
      transactionRepository = transactionRepository
    )
  }
}