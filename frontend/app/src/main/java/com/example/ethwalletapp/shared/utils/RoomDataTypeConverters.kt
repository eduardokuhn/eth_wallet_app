package com.example.ethwalletapp.shared.utils

import androidx.room.TypeConverter
import org.kethereum.model.Address
import java.math.BigInteger
import java.util.*

class RoomDataTypeConverters {
  @TypeConverter
  fun addressFromString(value: String?) = if (value == null) null else Address(value)

  @TypeConverter
  fun addressToString(address: Address?) = address?.hex

  @TypeConverter
  fun dateFromLong(value: Long?) = if (value == null) null else Date(value)

  @TypeConverter
  fun dateToLong(date: Date?) = date?.time

  @TypeConverter
  fun bigintegerFromByteArray(value: ByteArray?) = if (value == null) null else BigInteger(value)

  @TypeConverter
  fun bigIntegerToByteArray(bigInteger: BigInteger?) = bigInteger?.toByteArray()
}