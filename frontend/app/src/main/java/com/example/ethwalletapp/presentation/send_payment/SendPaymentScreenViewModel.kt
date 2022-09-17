package com.example.ethwalletapp.presentation.send_payment

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ethwalletapp.data.models.AccountEntry
import com.example.ethwalletapp.data.models.BalanceEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class SendPaymentScreenUIState(
  // SendToView - START
  val accounts: MutableList<AccountEntry> = mutableListOf(),
  val fromAccount: AccountEntry? = null,
  val balances: MutableList<BalanceEntry> = mutableListOf(),
  val fromAccountBalance: BalanceEntry? = null,
  val toAccountInput: String = "",
  val toAccountInputHelperText: String = "",
  val isToAccountInputValid: Boolean = true,
  val toOwnAccount: AccountEntry? = null,
  // SendToView - END
)

interface ISendPaymentScreenViewModel {
  var uiState: MutableState<SendPaymentScreenUIState>
  fun setFromAccount(account: AccountEntry, balance: BalanceEntry?)
  fun isFromAccountSelected(account: AccountEntry): Boolean
  fun setToAccountInput(value: String)
  fun setToOwnAccount(account: AccountEntry?, balance: BalanceEntry?)
  fun isToOwnAccountSelected(account: AccountEntry): Boolean
}

class SendPaymentScreenViewModelMock : ISendPaymentScreenViewModel {
  override var uiState = mutableStateOf(SendPaymentScreenUIState())
  override fun setFromAccount(account: AccountEntry, balance: BalanceEntry?) {}
  override fun isFromAccountSelected(account: AccountEntry): Boolean { return true }
  override fun setToAccountInput(value: String) {}
  override fun setToOwnAccount(account: AccountEntry?, balance: BalanceEntry?) {}
  override fun isToOwnAccountSelected(account: AccountEntry): Boolean { return true }
}

@HiltViewModel
class SendPaymentScreenViewModel @Inject constructor(

) : ViewModel(), ISendPaymentScreenViewModel {
  override var uiState = mutableStateOf(SendPaymentScreenUIState())

  override fun setFromAccount(account: AccountEntry, balance: BalanceEntry?) {
    TODO("Not yet implemented")
  }

  override fun isFromAccountSelected(account: AccountEntry): Boolean {
    TODO("Not yet implemented")
  }

  override fun setToAccountInput(value: String) {
    TODO("Not yet implemented")
  }

  override fun setToOwnAccount(account: AccountEntry?, balance: BalanceEntry?) {
    TODO("Not yet implemented")
  }

  override fun isToOwnAccountSelected(account: AccountEntry): Boolean {
    TODO("Not yet implemented")
  }
}