package com.example.ethwalletapp.shared.utils

import org.kethereum.ETH_IN_WEI
import java.math.BigInteger
import kotlin.math.pow

class EthereumUnitConverter {
  companion object {
    fun weiToEther(value: BigInteger): BigInteger {
      return value / ETH_IN_WEI
    }

    fun etherToWei(value: BigInteger): BigInteger {
      return value * ETH_IN_WEI
    }
  }
}