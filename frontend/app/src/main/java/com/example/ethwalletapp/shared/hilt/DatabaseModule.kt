package com.example.ethwalletapp.shared.hilt

import android.content.Context
import androidx.room.Room
import com.example.ethwalletapp.data.data_sources.daos.AccountDao
import com.example.ethwalletapp.data.data_sources.LocalAppDatabase
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
  fun provideLocalAppDatabase(@ApplicationContext context: Context) : LocalAppDatabase {
    return Room.databaseBuilder(
      context,
      LocalAppDatabase::class.java,
      "crypto-wallet-local-database"
    ).build()
  }
}