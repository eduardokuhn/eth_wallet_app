package com.example.ethwalletapp.presentation.token_overview

import android.view.View
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ethwalletapp.data.models.AccountEntry
import com.example.ethwalletapp.data.models.BalanceEntry
import com.example.ethwalletapp.data.models.TransactionEntry
import com.example.ethwalletapp.data.repositories.IAccountRepository
import com.example.ethwalletapp.data.repositories.IBalanceRepository
import com.example.ethwalletapp.data.repositories.ITransactionRepository
import com.example.ethwalletapp.data.services.IEthereumNetworkService
import com.example.ethwalletapp.shared.utils.NetworkResult
import com.example.ethwalletapp.shared.utils.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.kethereum.model.Address
import javax.inject.Inject

data class TokenOverviewUIState(
  val selectedAccount: AccountEntry? = null,
  val selectedAccountBalance: BalanceEntry? = null,
  val ethUsdPrice: Double? = 0.0,
  val transactions: MutableList<TransactionEntry> = mutableListOf(),
  val viewState: ViewState = ViewState.Unknown,
)

interface ITokenOverviewViewModel {
  var uiState: MutableState<TokenOverviewUIState>
  suspend fun refreshData()
}

class TokenOverviewViewModelMock : ITokenOverviewViewModel {
  override var uiState = mutableStateOf(TokenOverviewUIState())
  override suspend fun refreshData() {}
}

@HiltViewModel
class TokenOverviewViewModel @Inject constructor(
  private val ethereumNetworkService: IEthereumNetworkService,
  private val accountRepository: IAccountRepository,
  private val balanceRepository: IBalanceRepository,
  private val transactionRepository: ITransactionRepository,
  private val savedStateHandle: SavedStateHandle
) : ViewModel(), ITokenOverviewViewModel {
  override var uiState = mutableStateOf(TokenOverviewUIState())

  init { viewModelScope.launch { refreshData() } }

  override suspend fun refreshData() {
    loadAccount()
    loadBalance()
    loadTransactions()
    getEthereumLastPrice()
  }

  private suspend fun loadAccount() {
    uiState.value = uiState.value.copy(viewState = ViewState.Loading)

    val selectedAccountAddress: String? = savedStateHandle["selectedAccountAddress"]

    if (selectedAccountAddress != null) {
      val account = accountRepository.getAccount(Address(selectedAccountAddress))
      uiState.value = uiState.value.copy(
        selectedAccount = account,
        viewState = ViewState.Success
      )
    } else uiState.value = uiState.value.copy(viewState = ViewState.Error)
  }

  private suspend fun loadBalance() {
    uiState.value = uiState.value.copy(viewState = ViewState.Loading)

    if (uiState.value.selectedAccount != null) {
      val result = balanceRepository.getAddressBalance(uiState.value.selectedAccount!!.address)

      if (result is NetworkResult.Success) {
        uiState.value = uiState.value.copy(
          selectedAccountBalance = result.data,
          viewState = ViewState.Success
        )
      } else uiState.value = uiState.value.copy(viewState = ViewState.Error)
    } else uiState.value = uiState.value.copy(viewState = ViewState.Error)
  }

  private suspend fun loadTransactions() {
    uiState.value = uiState.value.copy(viewState = ViewState.Loading)

    if (uiState.value.selectedAccount != null) {
      val transactions = transactionRepository.getAddressTransactions(
        address = uiState.value.selectedAccount!!.address,
        chainId = ethereumNetworkService.selectedNetwork.chainId()
      ).toMutableList()

      uiState.value = uiState.value.copy(
        transactions = transactions,
        viewState = ViewState.Success
      )
    } else uiState.value = uiState.value.copy(viewState = ViewState.Error)
  }

  private suspend fun getEthereumLastPrice() {
    val result = balanceRepository.getPrice(true)

    if (result is NetworkResult.Success) {
      uiState.value = uiState.value.copy(
        ethUsdPrice = result.data,
        viewState = ViewState.Success
      )
    } else uiState.value = uiState.value.copy(viewState = ViewState.Error)
  }
}
