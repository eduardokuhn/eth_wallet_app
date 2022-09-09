package com.example.ethwalletapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import org.kethereum.model.Address
import java.math.BigInteger

@Entity(tableName = "balance", primaryKeys = ["address", "chain_id"])
data class BalanceEntry(
  val address: Address,
  @ColumnInfo(name = "chain_id")
  val chainId: BigInteger,
  @ColumnInfo(name = "token_address")
  val balance: BigInteger
)
