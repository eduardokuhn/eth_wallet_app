package com.example.ethwalletapp.presentation.token_overview

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.ethwalletapp.data.data_sources.TransactionsPagingSource
import com.example.ethwalletapp.data.models.AccountEntry
import com.example.ethwalletapp.data.models.BalanceEntry
import com.example.ethwalletapp.data.models.TransactionEntry
import com.example.ethwalletapp.data.repositories.IAccountRepository
import com.example.ethwalletapp.data.repositories.IBalanceRepository
import com.example.ethwalletapp.data.repositories.ITransactionRepository
import com.example.ethwalletapp.shared.utils.NetworkResult
import com.example.ethwalletapp.shared.utils.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.kethereum.model.Address
import javax.inject.Inject

data class TokenOverviewUIState(
  val selectedAccount: AccountEntry? = null,
  val selectedAccountBalance: BalanceEntry? = null,
  val ethUsdPrice: Double? = 0.0,
  val transactions: Flow<PagingData<TransactionEntry>> = MutableStateFlow(PagingData.empty()),
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
    getEthereumLastPrice()
    loadTransactions()
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

  private fun loadTransactions() {
    val transactions = Pager(PagingConfig(pageSize = 1)) {
      TransactionsPagingSource(
        transactionRepository,
        uiState.value.selectedAccount?.address ?: Address("")
      )
    }.flow.cachedIn(viewModelScope)

    uiState.value = uiState.value.copy(transactions = transactions)
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
