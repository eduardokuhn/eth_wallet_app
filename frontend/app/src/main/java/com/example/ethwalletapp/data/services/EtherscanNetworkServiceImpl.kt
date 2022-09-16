package com.example.ethwalletapp.data.services

import androidx.compose.ui.graphics.Color
import com.example.ethwalletapp.shared.network.HostSelectionInterceptor
import com.example.ethwalletapp.shared.theme.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.math.BigInteger
import javax.inject.Inject
import javax.inject.Singleton

enum class EthereumNetwork {
  Mainnet,
  Kovan,
  Goerli,
  Rinkeby,
  Ropsten,
  Sepolia;

  fun chainId(): BigInteger {
    return when (this) {
      Mainnet -> BigInteger.valueOf(1)
      Kovan -> BigInteger.valueOf(42)
      Goerli -> BigInteger.valueOf(5)
      Rinkeby -> BigInteger.valueOf(4)
      Ropsten -> BigInteger.valueOf(3)
      Sepolia -> BigInteger.valueOf(11155111)
    }
  }

  fun color(): Color {
    return when (this) {
      Mainnet -> Blue5
      Kovan -> Yellow5
      Goerli -> Green5
      Rinkeby -> Pink
      Ropsten -> Red5
      Sepolia -> Turquoise5
    }
  }
}

interface IEthereumNetworkService {
  var selectedNetwork: EthereumNetwork
  fun selectNetwork(network: EthereumNetwork)
}

@Singleton
class EtherscanNetworkServiceImpl @Inject constructor(
  private val hostSelectionInterceptor: HostSelectionInterceptor
) : IEthereumNetworkService {
  override var selectedNetwork: EthereumNetwork = EthereumNetwork.Rinkeby

  override fun selectNetwork(network: EthereumNetwork) {
    selectedNetwork = network
    val baseUrl = when (selectedNetwork) {
      EthereumNetwork.Mainnet -> "https://api.etherscan.io/"
      EthereumNetwork.Kovan -> "https://api-kovan.etherscan.io/"
      EthereumNetwork.Goerli -> "https://api-goerli.etherscan.io/"
      EthereumNetwork.Rinkeby -> "https://api-rinkeby.etherscan.io/"
      EthereumNetwork.Ropsten -> "https://api-ropsten.etherscan.io/"
      EthereumNetwork.Sepolia -> "https://api-sepolia.etherscan.io/"
    }
    hostSelectionInterceptor.setHostBaseUrl(baseUrl)
  }
}