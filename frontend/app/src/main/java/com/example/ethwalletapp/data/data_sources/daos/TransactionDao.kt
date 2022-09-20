package com.example.ethwalletapp.data.data_sources.daos

import androidx.room.*
import com.example.ethwalletapp.data.models.TransactionEntry
import org.kethereum.model.Address
import java.math.BigInteger

@Dao
interface TransactionDao {
  @Query("SELECT * FROM `transaction` WHERE hash LIKE :hash")
  suspend fun transaction(hash: String): TransactionEntry

  @Query("SELECT * FROM `transaction` WHERE \"from\" LIKE :address AND \"chain\" LIKE :chainId ORDER BY nonce DESC")
  suspend fun transactions(address: Address, chainId: BigInteger): List<TransactionEntry>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun add(transaction: TransactionEntry)

  @Update
  suspend fun update(editedTransaction: TransactionEntry)
}