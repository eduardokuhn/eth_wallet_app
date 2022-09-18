package com.example.ethwalletapp.presentation.send_payment

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ethwalletapp.data.models.AccountEntry
import com.example.ethwalletapp.data.models.BalanceEntry
import com.example.ethwalletapp.data.repositories.IAccountRepository
import com.example.ethwalletapp.data.repositories.IBalanceRepository
import com.example.ethwalletapp.data.services.IAccountService
import com.example.ethwalletapp.shared.utils.EthereumUnitConverter
import com.example.ethwalletapp.shared.utils.NetworkResult
import com.example.ethwalletapp.shared.utils.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.BigInteger
import javax.inject.Inject

data class SendPaymentScreenUIState(
  // SendToView - START
  val accounts: MutableList<AccountEntry> = mutableListOf(),
  val fromAccount: AccountEntry? = null,
  val balances: MutableList<BalanceEntry> = mutableListOf(),
  val fromAccountBalance: BalanceEntry? = null,
  val ethUsdPrice: Double? = null,
  val toAccountInput: String = "",
  val toAccountInputHelperText: String? = null,
  val isToAccountInputValid: Boolean = true,
  val toOwnAccount: AccountEntry? = null,
  val viewState: ViewState = ViewState.Unknown,
  // SendToView - END

  // AmountView - START
  val hasSufficientFunds: Boolean = true,
  val valueInputInEther: Double? = 0.0,
  val valueInputInUsd: Double? = 0.0,
  val valueInput: String = "0",
  val isValueInputInUsd: Boolean = false,
  // AmountView - END
)

interface ISendPaymentScreenViewModel {
  var uiState: MutableState<SendPaymentScreenUIState>
  // SendToView - START
  fun setFromAccount(account: AccountEntry, balance: BalanceEntry?)
  fun isFromAccountSelected(account: AccountEntry): Boolean
  fun setToAccountInput(value: String)
  fun validateToAccountInput(): Boolean
  fun setToOwnAccount(account: AccountEntry?, balance: BalanceEntry?)
  fun isToOwnAccountSelected(account: AccountEntry): Boolean
  // SendToView - END

  // AmountView - START
  fun setValueInput(value: String)
  fun toggleIsValueInputInUsd()
  fun useMax()
  // AmountView - END
}

class SendPaymentScreenViewModelMock : ISendPaymentScreenViewModel {
  override var uiState = mutableStateOf(SendPaymentScreenUIState())
  override fun setFromAccount(account: AccountEntry, balance: BalanceEntry?) {}
  override fun isFromAccountSelected(account: AccountEntry): Boolean { return true }
  override fun setToAccountInput(value: String) {}
  override fun validateToAccountInput(): Boolean { return true }
  override fun setToOwnAccount(account: AccountEntry?, balance: BalanceEntry?) {}
  override fun isToOwnAccountSelected(account: AccountEntry): Boolean { return true }
  override fun setValueInput(value: String) {}
  override fun toggleIsValueInputInUsd() {}
  override fun useMax() {}
}

@HiltViewModel
class SendPaymentScreenViewModel @Inject constructor(
  private val savedStateHandle: SavedStateHandle,
  private val accountService: IAccountService,
  private val accountRepository: IAccountRepository,
  private val balanceRepository: IBalanceRepository,
) : ViewModel(), ISendPaymentScreenViewModel {
  override var uiState = mutableStateOf(SendPaymentScreenUIState())

  init {
    viewModelScope.launch {
      loadAccounts()
      loadBalances()
      getEthereumLastPrice()
    }
  }

  override fun setFromAccount(account: AccountEntry, balance: BalanceEntry?) {
    uiState.value = uiState.value.copy(
      fromAccount = account,
      fromAccountBalance = balance
    )
  }

  override fun isFromAccountSelected(account: AccountEntry): Boolean {
    return if (uiState.value.fromAccount != null)
      account.address == uiState.value.fromAccount!!.address
    else false
  }

  override fun setToAccountInput(value: String) {
    uiState.value = uiState.value.copy(toAccountInput = value)
  }

  override fun validateToAccountInput(): Boolean {
    return if (uiState.value.toOwnAccount == null) {
      val isValid = accountService.validateAddress(uiState.value.toAccountInput)
      uiState.value = uiState.value.copy(
        isToAccountInputValid = isValid,
        toAccountInputHelperText = if (!isValid) "Invalid public address" else null
      )
      return isValid
    } else true
  }

  override fun setToOwnAccount(account: AccountEntry?, balance: BalanceEntry?) {
    uiState.value = uiState.value.copy(
      toOwnAccount = account,
      toAccountInput = "",
      toAccountInputHelperText = null,
      isToAccountInputValid = true,
    )
  }

  override fun isToOwnAccountSelected(account: AccountEntry): Boolean {
    return if (uiState.value.toOwnAccount != null)
      account.address == uiState.value.toOwnAccount!!.address
    else false
  }

  private suspend fun loadAccounts() {
    uiState.value = uiState.value.copy(viewState = ViewState.Loading)
    val accounts = accountRepository.getAllAccounts()

    if (accounts.isNotEmpty()) {
      val fromAccountAddress: String? = savedStateHandle["fromAccountAddress"]

      uiState.value = uiState.value.copy(
        accounts = accounts.toMutableList(),
        fromAccount = accounts.firstOrNull { account -> account.address.hex == fromAccountAddress },
        viewState = ViewState.Success
      )
    } else uiState.value = uiState.value.copy(viewState = ViewState.Error)
  }

  private suspend fun loadBalances() {
    uiState.value = uiState.value.copy(viewState = ViewState.Loading)

    if (uiState.value.accounts.isNotEmpty()) {
      val addresses = uiState.value.accounts.map { account ->
        account.address
      }

      val result = balanceRepository.getAddressesBalance(addresses, true)

      if (result is NetworkResult.Success && result.data.isNotEmpty()) {
        val balances = result.data

        uiState.value = uiState.value.copy(
          balances = balances.toMutableList(),
          fromAccountBalance = balances.firstOrNull {
              balance -> balance.address == uiState.value.fromAccount?.address
          },
          viewState = ViewState.Success
        )
      } else uiState.value = uiState.value.copy(viewState = ViewState.Error)
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

  override fun setValueInput(value: String) {
    if (uiState.value.isValueInputInUsd) {
      uiState.value = uiState.value.copy(
        valueInputInEther = uiState.value.ethUsdPrice?.let { value.toDoubleOrNull()?.div(it) },
        valueInputInUsd = value.toDoubleOrNull(),
        valueInput = value
      )
    } else {
      uiState.value = uiState.value.copy(
        valueInputInEther = value.toDoubleOrNull(),
        valueInputInUsd = uiState.value.ethUsdPrice?.let { value.toDoubleOrNull()?.times(it) },
        valueInput = value
      )
    }
    validateFunds()
  }

  override fun toggleIsValueInputInUsd() {
    val isInUsd = !uiState.value.isValueInputInUsd
    uiState.value = uiState.value.copy(
      isValueInputInUsd = isInUsd,
      valueInput =
        if (!isInUsd) uiState.value.valueInputInEther.toString()
        else uiState.value.valueInputInUsd.toString(),
    )
  }

  override fun useMax() {
    val maxEther = EthereumUnitConverter.weiToEther(
      uiState.value.fromAccountBalance?.balance ?: BigInteger.valueOf(0)
    ).toDouble()

    uiState.value = uiState.value.copy(
      valueInputInEther = maxEther,
      valueInputInUsd = uiState.value.ethUsdPrice?.let { maxEther.times(it) },
      valueInput =
        if (!uiState.value.isValueInputInUsd) maxEther.toString()
        else (uiState.value.ethUsdPrice?.let { maxEther.div(it) }).toString()
    )
  }

  private fun validateFunds() {
    if (uiState.value.valueInputInEther != null && uiState.value.fromAccountBalance != null) {
      if (
        uiState.value.valueInputInEther!! >
        EthereumUnitConverter.weiToEther(uiState.value.fromAccountBalance!!.balance).toDouble()
      ) {
        uiState.value = uiState.value.copy(hasSufficientFunds = false)
      } else {
        uiState.value = uiState.value.copy(hasSufficientFunds = true)
      }
    }
  }
}