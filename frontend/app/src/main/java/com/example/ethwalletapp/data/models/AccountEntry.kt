package com.example.ethwalletapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.kethereum.model.Address

@Entity(tableName = "account")
data class AccountEntry(
  @PrimaryKey
  val address: Address,
  val name: String,
  val color: String,
  @ColumnInfo(name = "address_index")
  val addressIndex: Int
)
