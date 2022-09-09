package com.example.ethwalletapp.data.data_sources.daos

import androidx.room.*
import com.example.ethwalletapp.data.models.AccountEntry

@Dao
interface AccountDao {
  @Query("SELECT * FROM account")
  suspend fun all(): List<AccountEntry>

  @Query("SELECT COUNT(address) FROM account")
  suspend fun count(): Int

  @Query("SELECT MAX(address_index) FROM account")
  suspend fun latestAddressIndex(): Int

  @Insert
  suspend fun add(account: AccountEntry)

  @Update
  suspend fun update(editedAccount: AccountEntry)
}