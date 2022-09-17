package com.example.ethwalletapp.presentation.home.wallet

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ethwalletapp.data.models.AccountEntry
import com.example.ethwalletapp.data.models.BalanceEntry
import com.example.ethwalletapp.data.repositories.IAccountRepository
import com.example.ethwalletapp.data.repositories.IBalanceRepository
import com.example.ethwalletapp.data.services.EthereumNetwork
import com.example.ethwalletapp.data.services.IAccountService
import com.example.ethwalletapp.data.services.IEthereumNetworkService
import com.example.ethwalletapp.shared.utils.NetworkResult
import com.example.ethwalletapp.shared.utils.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WalletViewUIState(
  // WalletView - START
  val selectedNetwork: EthereumNetwork = EthereumNetwork.Rinkeby,
  val accounts: MutableList<AccountEntry> = mutableListOf(),
  val selectedAccount: AccountEntry? = null,
  val balances: MutableList<BalanceEntry> = mutableListOf(),
  val selectedAccountBalance: BalanceEntry? = null,
  val ethUsdPrice: Double? = null,
  val viewState: ViewState = ViewState.Unknown,
  // WalletView - END

  // CreateAccountBottomSheetContentView - STAR
  val newAccountName: String = "",
  val createNewAccountHasError: Boolean = false,
  // CreateAccountBottomSheetContentView - END

  // ImportAccountBottomSheetContentView - START
  val importAccountPrivateKey: String = "",
  val isImportAccountPrivateKeyValid: Boolean = true,
  val importAccountHasError: Boolean = false
  // ImportAccountBottomSheetContentView - END
)

interface IWalletViewViewModel {
  var uiState: MutableState<WalletViewUIState>
  fun setSelectedNetwork(network: EthereumNetwork)
  fun isNetworkSelected(network: EthereumNetwork): Boolean
  fun selectAccount(account: AccountEntry, balance: BalanceEntry?)
  fun isAccountSelected(account: AccountEntry): Boolean
  suspend fun loadAccounts()
  suspend fun loadBalances()
  suspend fun getEthereumLastPrice()
  fun setNewAccountName(value: String)
  suspend fun createNewAccount(): Boolean
  fun setImportAccountPrivateKey(value: String)
  suspend fun importAccount(): Boolean
}

class WalletViewViewModelMock : IWalletViewViewModel {
  override var uiState = mutableStateOf(WalletViewUIState())
  override fun setSelectedNetwork(network: EthereumNetwork) {}
  override fun isNetworkSelected(network: EthereumNetwork): Boolean { return true }
  override fun selectAccount(account: AccountEntry, balance: BalanceEntry?) {}
  override fun isAccountSelected(account: AccountEntry): Boolean { return true }
  override suspend fun loadAccounts() {}
  override suspend fun loadBalances() {}
  override suspend fun getEthereumLastPrice() {}
  override fun setNewAccountName(value: String) {}
  override suspend fun createNewAccount(): Boolean { return true }
  override fun setImportAccountPrivateKey(value: String) {}
  override suspend fun importAccount(): Boolean { return true }
}

@HiltViewModel
class WalletViewViewModel @Inject constructor(
  private val ethereumNetworkService: IEthereumNetworkService,
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

  override fun setSelectedNetwork(network: EthereumNetwork) {
    uiState.value = uiState.value.copy(selectedNetwork = network)
    ethereumNetworkService.selectNetwork(network)
    viewModelScope.launch { loadBalances() }
  }

  override fun isNetworkSelected(network: EthereumNetwork): Boolean {
    return network == uiState.value.selectedNetwork
  }

  override fun selectAccount(account: AccountEntry, balance: BalanceEntry?) {
    setSelectedAccount(account)
    setSelectedAccountBalance(balance)
  }

  override fun isAccountSelected(account: AccountEntry): Boolean {
    return if (uiState.value.selectedAccount != null)
      account.address == uiState.value.selectedAccount!!.address
    else false
  }

  private fun setSelectedAccount(account: AccountEntry) {
    uiState.value = uiState.value.copy(selectedAccount = account)
  }

  private fun setSelectedAccountBalance(balance: BalanceEntry?) {
    uiState.value = uiState.value.copy(selectedAccountBalance = balance)
  }

  override suspend fun loadAccounts() {
    uiState.value = uiState.value.copy(viewState = ViewState.Loading)
    val accounts = accountRepository.getAllAccounts()

    if (accounts.isNotEmpty()) {
      uiState.value = uiState.value.copy(
        accounts = accounts.toMutableList(),
        selectedAccount = accounts.firstOrNull { account -> account.addressIndex == 0 },
        viewState = ViewState.Success
      )
    } else uiState.value = uiState.value.copy(viewState = ViewState.Error)
  }

  override suspend fun loadBalances() {
    uiState.value = uiState.value.copy(viewState = ViewState.Loading)

    if (uiState.value.accounts.isNotEmpty()) {
      val addresses = uiState.value.accounts.map { account ->
        account.address
      }

      val result = balanceRepository.getAddressesBalance(addresses)

      if (result is NetworkResult.Success && result.data.isNotEmpty()) {
        val balances = result.data

        uiState.value = uiState.value.copy(
          balances = balances.toMutableList(),
          selectedAccountBalance = balances.firstOrNull {
              balance -> balance.address == uiState.value.selectedAccount?.address
          },
          viewState = ViewState.Success
        )
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

  override fun setNewAccountName(value: String) {
    uiState.value = uiState.value.copy(newAccountName = value)
  }

  override suspend fun createNewAccount(): Boolean {
    val createdAccount = accountService.createChildAccount(
      uiState.value.newAccountName.ifEmpty { null }
    )

    return if (createdAccount != null) {
      val createdAccountBalance = balanceRepository.createAddressBalance(createdAccount.address)

      uiState.value = uiState.value.copy(
        accounts = uiState.value.accounts.plus(createdAccount).toMutableList(),
        balances = uiState.value.balances.plus(createdAccountBalance).toMutableList(),
        newAccountName = "",
        createNewAccountHasError = false
      )
      true
    } else {
      uiState.value = uiState.value.copy(createNewAccountHasError = true)
      false
    }
  }

  override fun setImportAccountPrivateKey(value: String) {
    uiState.value = uiState.value.copy(importAccountPrivateKey = value)
  }

  override suspend fun importAccount(): Boolean {
    val importedAccount = accountService.importExternAccount(uiState.value.importAccountPrivateKey)

    return if (importedAccount != null) {
      val result = balanceRepository.getAddressBalance(importedAccount.address)

      return if (result is NetworkResult.Success) {
        val importedAccountBalance = result.data
        uiState.value = uiState.value.copy(
          accounts = uiState.value.accounts.plus(importedAccount).toMutableList(),
          balances = uiState.value.balances.plus(importedAccountBalance).toMutableList(),
          importAccountPrivateKey = "",
          isImportAccountPrivateKeyValid = true,
          importAccountHasError = false
        )

        true
      } else {
        uiState.value = uiState.value.copy(importAccountHasError = true)
        false
      }
    } else {
      uiState.value = uiState.value.copy(isImportAccountPrivateKeyValid = false)
      false
    }
  }
}