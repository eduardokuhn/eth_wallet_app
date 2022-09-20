package com.example.ethwalletapp.data.data_sources

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ethwalletapp.data.data_sources.daos.AccountDao
import com.example.ethwalletapp.data.data_sources.daos.BalanceDao
import com.example.ethwalletapp.data.data_sources.daos.TransactionDao
import com.example.ethwalletapp.data.models.AccountEntry
import com.example.ethwalletapp.data.models.BalanceEntry
import com.example.ethwalletapp.data.models.TransactionEntry
import com.example.ethwalletapp.shared.utils.RoomDataTypeConverters

@Database(
  entities = [
    AccountEntry::class,
    BalanceEntry::class,
    TransactionEntry::class
  ],
  version = 1
)
@TypeConverters(RoomDataTypeConverters::class)
abstract class LocalAppDatabase: RoomDatabase() {
  abstract fun accountDao(): AccountDao
  abstract fun balanceDao(): BalanceDao
  abstract fun transactionDao(): TransactionDao
}