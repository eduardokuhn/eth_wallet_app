package com.example.ethwalletapp.presentation.home.wallet

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ethwalletapp.data.models.AccountEntry
import com.example.ethwalletapp.data.models.BalanceEntry
import com.example.ethwalletapp.data.repositories.IAccountRepository
import com.example.ethwalletapp.data.repositories.IBalanceRepository
import com.example.ethwalletapp.data.services.IAccountService
import com.example.ethwalletapp.shared.utils.NetworkResult
import com.example.ethwalletapp.shared.utils.ViewState
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
  // WalletView
  val network: EthereumNetwork = EthereumNetwork.Rinkeby,
  val accounts: MutableList<AccountEntry> = mutableListOf(),
  val selectedAccount: AccountEntry? = null,
  val balances: MutableList<BalanceEntry> = mutableListOf(),
  val selectedAccountBalance: BalanceEntry? = null,
  val ethUsdPrice: Double? = null,
  val viewState: ViewState = ViewState.Unknown,
  // CreateAccountBottomSheetContentView
  val newAccountName: String = "",
  val createNewAccountHasError: Boolean = false
)

interface IWalletViewViewModel {
  var uiState: MutableState<WalletViewUIState>
  fun setNetwork(network: EthereumNetwork)
  fun setSelectedAccount(account: AccountEntry)
  suspend fun loadAccounts()
  suspend fun loadBalances()
  suspend fun getEthereumLastPrice()
  fun setNewAccountName(value: String)
  suspend fun createNewAccount(): Boolean
}

class WalletViewViewModelMock : IWalletViewViewModel {
  override var uiState = mutableStateOf(WalletViewUIState())
  override fun setNetwork(network: EthereumNetwork) {}
  override fun setSelectedAccount(account: AccountEntry) {}
  override suspend fun loadAccounts() {}
  override suspend fun loadBalances() {}
  override suspend fun getEthereumLastPrice() {}
  override fun setNewAccountName(value: String) {}
  override suspend fun createNewAccount(): Boolean { return true }
}

@HiltViewModel
class WalletViewViewModel @Inject constructor(
  private val accountService: IAccountService,
  private val accountRepository: IAccountRepository,
  private val balanceRepository: IBalanceRepository
) : ViewModel(), IWalletViewViewModel {
  override var uiState = mutableStateOf(WalletViewUIState())

  init {
    viewModelScope.launch {
      loadAccounts()
      loadBalances()
      getEthereumLastPrice()
    }
  }

  override fun setNetwork(network: EthereumNetwork) {
    uiState.value = uiState.value.copy(network = network)
  }

  override fun setSelectedAccount(account: AccountEntry) {
    uiState.value = uiState.value.copy(selectedAccount = account)
  }

  override suspend fun loadAccounts() {
    uiState.value = uiState.value.copy(viewState = ViewState.Loading)
    val accounts = accountRepository.getAllAccounts()

    if (accounts.isNotEmpty()) {
      uiState.value = uiState.value.copy(
        accounts = accounts.toMutableList(),
        selectedAccount = accounts.singleOrNull { account -> account.addressIndex == 0 },
        viewState = ViewState.Success
      )
    } else uiState.value = uiState.value.copy(viewState = ViewState.Error)
  }

  override suspend fun loadBalances() {
    uiState.value = uiState.value.copy(viewState = ViewState.Loading)

    if (uiState.value.accounts != null && uiState.value.accounts.isNotEmpty()) {
      val addresses = uiState.value.accounts.map { account ->
        account.address
      }

      val result = balanceRepository.getAddressesBalance(addresses)

      if (result is NetworkResult.Success) {
        val balances = result.data
        println("Balances: $balances")

        if (balances != null && balances.isNotEmpty()) {
          uiState.value = uiState.value.copy(
            balances = balances.toMutableList(),
            selectedAccountBalance = balances.singleOrNull {
                balance -> balance.address == uiState.value.selectedAccount!!.address
            },
            viewState = ViewState.Success
          )
        }
        else uiState.value = uiState.value.copy(viewState = ViewState.Error)
      } else uiState.value = uiState.value.copy(viewState = ViewState.Error)
    } else uiState.value = uiState.value.copy(viewState = ViewState.Error)
  }

  override suspend fun getEthereumLastPrice() {
    val result = balanceRepository.getPrice()

    if (result is NetworkResult.Success) {
      uiState.value = uiState.value.copy(
        ethUsdPrice = result.data,
        viewState = ViewState.Success
      )
    } else uiState.value = uiState.value.copy(viewState = ViewState.Error)
  }

  override fun setNewAccountName(value: String) { uiState.value = uiState.value.copy(newAccountName = value) }

  override suspend fun createNewAccount(): Boolean {
    val createdAccount = accountService.createChildAccount()

    return if (createdAccount != null) {
      uiState.value = uiState.value.copy(
        accounts = uiState.value.accounts.plus(createdAccount).toMutableList(),
        newAccountName = "",
        createNewAccountHasError = false
      )
      true
    } else {
      uiState.value = uiState.value.copy(createNewAccountHasError = true)
      false
    }
  }
}