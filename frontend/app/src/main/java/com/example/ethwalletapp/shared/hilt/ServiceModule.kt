package com.example.ethwalletapp.shared.hilt

import com.example.ethwalletapp.data.repositories.IAccountRepository
import com.example.ethwalletapp.data.services.AccountService
import com.example.ethwalletapp.data.services.IAccountService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.kethereum.keystore.api.KeyStore

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
  @Provides
  fun provideServiceModule(
    keyStore: KeyStore,
    accountRepository: IAccountRepository
  ) : IAccountService {
    return AccountService(keyStore, accountRepository)
  }
}