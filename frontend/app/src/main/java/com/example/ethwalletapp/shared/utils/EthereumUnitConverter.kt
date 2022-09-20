package com.example.ethwalletapp.shared.utils

import org.kethereum.ETH_IN_WEI
import java.math.BigInteger

class EthereumUnitConverter {
  companion object {
    fun weiToEther(value: BigInteger): BigInteger {
      return value / ETH_IN_WEI
    }

    fun gweiToEther(value: BigInteger): BigInteger {
      println("GweiToEther: ${value / BigInteger("1000000000")}")
      return value / BigInteger("1000000000")
    }

    fun etherToWei(value: BigInteger): BigInteger {
      return value * ETH_IN_WEI
    }
  }
}