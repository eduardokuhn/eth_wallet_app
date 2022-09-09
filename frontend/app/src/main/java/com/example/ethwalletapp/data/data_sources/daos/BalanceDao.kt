package com.example.ethwalletapp.data.data_sources.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.ethwalletapp.data.models.BalanceEntry
import org.kethereum.model.Address

@Dao
interface BalanceDao {
  @Query("SELECT * FROM balance WHERE address LIKE :address")
  suspend fun balance(address: Address): BalanceEntry

  @Query("SELECT * FROM balance WHERE address IN (:addresses)")
  suspend fun balances(addresses: List<Address>): List<BalanceEntry>

  @Insert
  suspend fun add(balance: BalanceEntry)

  @Update
  suspend fun update(editedBalance: BalanceEntry)
}