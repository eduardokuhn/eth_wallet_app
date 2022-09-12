package com.example.ethwalletapp.presentation.wallet

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ethwalletapp.data.models.AccountEntry
import com.example.ethwalletapp.data.models.BalanceEntry
import com.example.ethwalletapp.data.repositories.IAccountRepository
import com.example.ethwalletapp.data.repositories.IBalanceRepository
import com.example.ethwalletapp.shared.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class EthereumNetwork {
  object Mainnet: EthereumNetwork()
  object Kovan: EthereumNetwork()
  object Goerli: EthereumNetwork()
  object Rinkeby: EthereumNetwork()
  object Ropsten: EthereumNetwork()
  object Sepolia: EthereumNetwork()

  override fun toString(): String {
    return when (this) {
      Mainnet -> "Mainnet"
      Kovan -> "Kovan"
      Goerli -> "Goerli"
      Rinkeby -> "Rinkeby"
      Ropsten -> "Ropsten"
      Sepolia -> "Sepolia"
    }
  }
}

data class WalletViewUIState(
  val network: EthereumNetwork = EthereumNetwork.Rinkeby,
  val accounts: List<AccountEntry>? = null,
  val selectedAccount: AccountEntry? = null,
  val balances: List<BalanceEntry>? = null,
  val selectedAccountBalance: BalanceEntry? = null,
  val etherUsdPrice: Double? = null,
  val hasNetworkError: Boolean = false,
)

interface IWalletViewViewModel {
  var uiState: MutableState<WalletViewUIState>
  fun setNetwork(network: EthereumNetwork)
  fun setSelectedAccount(account: AccountEntry)
  suspend fun loadAccounts()
  suspend fun loadBalances()
  suspend fun getEthereumLastPrice()
}

class WalletViewViewModelMock : IWalletViewViewModel {
  override var uiState = mutableStateOf(WalletViewUIState())
  override fun setNetwork(network: EthereumNetwork) {}
  override fun setSelectedAccount(account: AccountEntry) {}
  override suspend fun loadAccounts() {}
  override suspend fun loadBalances() {}
  override suspend fun getEthereumLastPrice() {}
}

@HiltViewModel
class WalletViewViewModel @Inject constructor(
  private val accountRepository: IAccountRepository,
  private val balanceRepository: IBalanceRepository
) : ViewModel(), IWalletViewViewModel {
  override var uiState = mutableStateOf(WalletViewUIState())

  init {
    viewModelScope.launch {
      loadAccounts()
      loadBalances()
    }
  }

  override fun setNetwork(network: EthereumNetwork) {
    uiState.value = uiState.value.copy(network = network)
  }

  override fun setSelectedAccount(account: AccountEntry) {
    uiState.value = uiState.value.copy(selectedAccount = account)
  }

  override suspend fun loadAccounts() {
    val accounts = accountRepository.getAllAccounts()
    uiState.value = uiState.value.copy(
      accounts = accounts,
      selectedAccount = accounts.single { account -> account.addressIndex == 0 }
    )
  }

  override suspend fun loadBalances() {
    var balances: List<BalanceEntry> = listOf()
    var hasNetworkError = false

    if (uiState.value.accounts != null) {
      val addresses = uiState.value.accounts!!.map { account ->
        account.address
      }

      when (val result = balanceRepository.getAddressesBalance(addresses)) {
        is NetworkResult.Success -> balances = result.data
        is NetworkResult.Error -> hasNetworkError = true
        is NetworkResult.Exception -> hasNetworkError = true
      }

      uiState.value = uiState.value.copy(
        balances = balances,
        selectedAccountBalance = balances.single {
          balance -> balance.address == uiState.value.selectedAccount.address
        },
        hasNetworkError = hasNetworkError
      )
    }
  }

  override suspend fun getEthereumLastPrice() {
    var price = 0.0
    var hasNetworkError = false

    when (val result = balanceRepository.getPrice()) {
      is NetworkResult.Success -> price = result.data
      is NetworkResult.Error -> hasNetworkError = true
      is NetworkResult.Exception -> hasNetworkError = true
    }

    uiState.value = uiState.value.copy(
      etherUsdPrice = price,
      hasNetworkError = hasNetworkError
    )
  }
}