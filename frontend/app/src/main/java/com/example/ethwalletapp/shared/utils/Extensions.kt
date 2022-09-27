package com.example.ethwalletapp.shared.utils

import android.icu.text.SimpleDateFormat
import org.kethereum.ETH_IN_WEI
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

val ETH_IN_GWEI = BigInteger("1000000000")

fun BigInteger.weiToEther(): BigDecimal {
  return BigDecimal(this).divide(BigDecimal(ETH_IN_WEI)).stripTrailingZeros()
}

fun BigDecimal.etherToWei(): BigInteger {
  return this.times(ETH_IN_WEI.toBigDecimal()).toBigIntegerExact()
}

fun BigInteger.gweiToEther(): BigDecimal {
  return BigDecimal(this).divide(BigDecimal(ETH_IN_GWEI)).stripTrailingZeros()
}

fun Long.toDateTime(): String {
  val instant = java.time.Instant.ofEpochSecond(this)
  val date = Date.from(instant)
  val format = SimpleDateFormat("MMM dd yyyy HH:mm")
  return format.format(date)
}