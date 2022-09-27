package com.example.ethwalletapp.data.data_sources.daos

import androidx.room.*
import com.example.ethwalletapp.data.models.AccountEntry
import com.example.ethwalletapp.data.models.BalanceEntry
import org.kethereum.model.Address

@Dao
interface AccountDao {
  @Query("SELECT * FROM account")
  suspend fun all(): List<AccountEntry>

  @Query("SELECT * FROM account WHERE address LIKE :address")
  suspend fun account(address: Address): AccountEntry

  @Query("SELECT COUNT(address) FROM account")
  suspend fun count(): Int

  @Query("SELECT MAX(address_index) FROM account")
  suspend fun latestAddressIndex(): Int

  @Query("DELETE FROM account")
  suspend fun deleteAll()

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun add(account: AccountEntry)

  @Update
  suspend fun update(editedAccount: AccountEntry)
}