package com.example.ethwalletapp.shared.hilt

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.ethwalletapp.data.data_sources.daos.AccountDao
import com.example.ethwalletapp.data.data_sources.LocalAppDatabase
import com.example.ethwalletapp.data.data_sources.daos.BalanceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
  @Singleton
  @Provides
  fun provideAccountDao(appDatabase: LocalAppDatabase) : AccountDao {
    return appDatabase.accountDao()
  }

  @Singleton
  @Provides
  fun provideBalanceDao(appDatabase: LocalAppDatabase) : BalanceDao {
    return appDatabase.balanceDao()
  }

  @Singleton
  @Provides
  fun provideLocalAppDatabase(@ApplicationContext context: Context) : LocalAppDatabase {
    return Room.databaseBuilder(
      context,
      LocalAppDatabase::class.java,
      "crypto-wallet-local-database"
    ).build()
  }

  @Singleton
  @Provides
  fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
    return context.getSharedPreferences("crypto-wallet-preferences", Context.MODE_PRIVATE)
  }
}