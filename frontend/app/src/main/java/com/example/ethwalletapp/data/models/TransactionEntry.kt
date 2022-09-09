package com.example.ethwalletapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.kethereum.DEFAULT_GAS_LIMIT
import org.kethereum.model.Address
import org.kethereum.model.SignatureData
import java.math.BigInteger

data class Transaction(
  val to: Address,
  val from: Address,
  val value: BigInteger?,
  @ColumnInfo(name = "gas_limit")
  val gasLimit: BigInteger? = DEFAULT_GAS_LIMIT,
  @ColumnInfo(name = "max_priority_fee_per_gas")
  val maxPriorityFeePerGas: BigInteger?,
  @ColumnInfo(name = "max_fee_per_gas")
  val maxFeePerGas: BigInteger?,
  val nonce: BigInteger,
  @ColumnInfo(name = "chain_id")
  val chainId: BigInteger
)

data class TransactionState(
  val signed: Boolean = false,
  @ColumnInfo(name = "is_pending")
  val isPending: Boolean = true,
  val error: String? = null,
)

@Entity(tableName = "transaction")
data class TransactionEntry(
  @PrimaryKey
  val hash: String,
  @Embedded
  val transaction: Transaction,
  @Embedded()
  val signatureData: SignatureData?,
  @Embedded()
  val state: TransactionState
)
