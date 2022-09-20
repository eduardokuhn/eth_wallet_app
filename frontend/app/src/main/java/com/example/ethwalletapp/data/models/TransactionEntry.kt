package com.example.ethwalletapp.data.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.kethereum.model.SignatureData
import org.kethereum.model.Transaction

enum class TransactionState {
  Pending,
  Confirmed,
  Failed;

  companion object {
    fun fromString(value: String): TransactionState {
      return when (value) {
        "0" -> Failed
        "1" -> Confirmed
        else -> Pending
      }
    }
  }
}

@Entity(tableName = "transaction")
data class TransactionEntry(
  @PrimaryKey
  val hash: String,
  @Embedded
  val transaction: Transaction,
  @Embedded()
  val signatureData: SignatureData?,
  val state: TransactionState
)
