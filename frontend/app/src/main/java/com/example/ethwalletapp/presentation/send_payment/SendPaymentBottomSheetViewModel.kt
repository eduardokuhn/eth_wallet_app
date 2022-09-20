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
import com.example.ethwalletapp.data.repositories.ITransactionRepository
import com.example.ethwalletapp.data.services.IAccountService
import com.example.ethwalletapp.data.services.ITransactionService
import com.example.ethwalletapp.shared.utils.EthereumUnitConverter
import com.example.ethwalletapp.shared.utils.NetworkResult
import com.example.ethwalletapp.shared.utils.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.kethereum.DEFAULT_GAS_LIMIT
import org.kethereum.model.Address
import java.math.BigInteger
import javax.inject.Inject

data class SendPaymentBottomSheetUIState(
  // SendToView - START
  val accounts: MutableList<AccountEntry> = mutableListOf(),
  val fromAccount: AccountEntry? = null,
  val balances: MutableList<BalanceEntry> = mutableListOf(),
  val fromAccountBalance: BalanceEntry? = null,
  val ethUsdPrice: Double? = 0.0,
  val toAccount: AccountEntry? = null,
  val toAccountInput: String = "",
  val toAccountInputHelperText: String? = null,
  val isToAccountInputValid: Boolean = true,
  val viewState: ViewState = ViewState.Unknown,
  // SendToView - END

  // AmountView - START
  val hasSufficientFunds: Boolean = true,
  val valueInputInEther: Double? = 0.0,
  val valueInputInUsd: Double? = 0.0,
  val valueInput: String = "0",
  val isValueInputInUsd: Boolean = false,
  // AmountView - END

  // ConfirmView - START
  // TODO bug
  val networkFeeInEther: BigInteger = EthereumUnitConverter.gweiToEther(DEFAULT_GAS_LIMIT * BigInteger.valueOf(200)),
  val totalValueInEther: BigInteger? = valueInputInEther?.let { BigInteger.valueOf(it.toLong()) + networkFeeInEther },
  val totalValueInUsd: Double? = ethUsdPrice?.let { totalValueInEther?.toDouble()?.times(it) },
  val confirmViewState: ViewState = ViewState.Unknown
  // ConfirmView - END
)

interface ISendPaymentBottomSheetViewModel {
  var uiState: MutableState<SendPaymentBottomSheetUIState>
  // SendToView - START
  fun setFromAccount(account: AccountEntry, balance: BalanceEntry?)
  fun isFromAccountSelected(account: AccountEntry): Boolean
  fun setToAccountInput(value: String)
  fun validateToAccountInput(): Boolean
  fun setToAccount(account: AccountEntry?, balance: BalanceEntry?)
  fun isToAccountSelected(account: AccountEntry): Boolean
  // SendToView - END

  // AmountView - START
  fun setValueInput(value: String)
  fun toggleIsValueInputInUsd()
  fun useMax()
  // AmountView - END

  // ConfirmView - START
  suspend fun sendTransaction(): Boolean
  // ConfirmView - END
}

class SendPaymentBottomSheetViewModelMock : ISendPaymentBottomSheetViewModel {
  override var uiState = mutableStateOf(SendPaymentBottomSheetUIState())
  override fun setFromAccount(account: AccountEntry, balance: BalanceEntry?) {}
  override fun isFromAccountSelected(account: AccountEntry): Boolean { return true }
  override fun setToAccountInput(value: String) {}
  override fun validateToAccountInput(): Boolean { return true }
  override fun setToAccount(account: AccountEntry?, balance: BalanceEntry?) {}
  override fun isToAccountSelected(account: AccountEntry): Boolean { return true }
  override fun setValueInput(value: String) {}
  override fun toggleIsValueInputInUsd() {}
  override fun useMax() {}
  override suspend fun sendTransaction(): Boolean { return true }
}

@HiltViewModel
class SendPaymentBottomSheetViewModel @Inject constructor(
  private val savedStateHandle: SavedStateHandle,
  private val accountService: IAccountService,
  private val accountRepository: IAccountRepository,
  private val balanceRepository: IBalanceRepository,
  private val transactionService: ITransactionService,
  private val transactionRepository: ITransactionRepository
) : ViewModel(), ISendPaymentBottomSheetViewModel {
  override var uiState = mutableStateOf(SendPaymentBottomSheetUIState())

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
    println("FromAccount: ${account.address.hex}")
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
    return if (uiState.value.toAccount == null) {
      val isValid = accountService.validateAddress(uiState.value.toAccountInput)
      var toAccount: AccountEntry? = null

      if (isValid) {
        toAccount = AccountEntry(
          address = Address(uiState.value.toAccountInput),
          name = "Undefined name",
          addressIndex = 9999
        )
      }

      uiState.value = uiState.value.copy(
        isToAccountInputValid = isValid,
        toAccountInputHelperText = if (!isValid) "Invalid public address" else null,
        toAccount = toAccount
      )
      return isValid
    } else true
  }

  override fun setToAccount(account: AccountEntry?, balance: BalanceEntry?) {
    uiState.value = uiState.value.copy(
      toAccount = account,
      toAccountInput = "",
      isToAccountInputValid = true,
      toAccountInputHelperText = null,
    )
  }

  override fun isToAccountSelected(account: AccountEntry): Boolean {
    return if (uiState.value.toAccount != null)
      account.address == uiState.value.toAccount!!.address
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

  override suspend fun sendTransaction(): Boolean {
   uiState.value = uiState.value.copy(confirmViewState = ViewState.Loading)

    return if (
      uiState.value.fromAccount != null &&
      uiState.value.toAccount != null &&
      uiState.value.valueInputInEther != null &&
      uiState.value.hasSufficientFunds
    ) {
      val transaction = transactionService.createTransaction(
        from = uiState.value.fromAccount!!,
        to = uiState.value.toAccount!!,
        value = uiState.value.valueInputInEther!!
      )

      if (transaction != null) {
        val result = transactionRepository.sendTransaction(transaction)

        if (result is NetworkResult.Success) {
          uiState.value.copy(confirmViewState = ViewState.Success)
          true
        } else {
          uiState.value = uiState.value.copy(confirmViewState = ViewState.Error)
          false
        }
      } else {
        uiState.value = uiState.value.copy(confirmViewState = ViewState.Error)
        false
      }
    } else {
      uiState.value = uiState.value.copy(confirmViewState = ViewState.Unknown)
      false
    }
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