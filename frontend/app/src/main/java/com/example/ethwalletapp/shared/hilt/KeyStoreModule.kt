package com.example.ethwalletapp.shared.hilt

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.kethereum.keystore.api.InitializingFileKeyStore
import org.kethereum.keystore.api.KeyStore
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KeyStoreModule {
  @Singleton
  @Provides
  fun provideKeyStore(@ApplicationContext context: Context) : KeyStore {
    val keyStore by lazy { InitializingFileKeyStore(File(context.filesDir,"keystore")) }
    return keyStore
  }
}