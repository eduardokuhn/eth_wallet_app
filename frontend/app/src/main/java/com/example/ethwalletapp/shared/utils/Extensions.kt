package com.example.ethwalletapp.shared.utils

import org.kethereum.ETH_IN_WEI
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

val ETH_IN_GWEI = BigInteger("1000000000")

fun BigInteger.weiToEther(): BigDecimal {
  return BigDecimal(this).divide(BigDecimal(ETH_IN_WEI)).stripTrailingZeros()
}

fun BigInteger.gweiToEther(): BigDecimal {
  return BigDecimal(this).divide(BigDecimal(ETH_IN_GWEI)).stripTrailingZeros()
}