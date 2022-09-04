package com.example.ethwalletapp.ui.create_account

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ethwalletapp.data.repositories.EthWalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.kethereum.crypto.toAddress
import javax.inject.Inject

sealed class CreateAccountStep {
  object CREATE_PASSWORD: CreateAccountStep()
  object SECURE_WALLET: CreateAccountStep()
  object CONFIRM_RECOVERY_PHRASE: CreateAccountStep()

  fun title(): String {
    return when(this) {
      CREATE_PASSWORD -> "Create Password"
      SECURE_WALLET -> "Secure Your Wallet"
      CONFIRM_RECOVERY_PHRASE -> "Confirm Recovery Phrase"
      else -> "Undefined."
    }
  }

  fun description(): String {
    return when(this) {
      CREATE_PASSWORD ->
        "This password will unlock your wallet only on this service."
      SECURE_WALLET ->
        "This is your recovery phrase. Write it down on a paper and keep it in a safe place. You'll be asked to re-enter this phrase (in order) on the next step."
      CONFIRM_RECOVERY_PHRASE ->
        "Type your recovery phrase."
      else -> "Undefined."
    }
  }

  fun index(): Int {
    return when(this) {
      CREATE_PASSWORD -> 0
      SECURE_WALLET -> 1
      CONFIRM_RECOVERY_PHRASE -> 2
      else -> 0
    }
  }
}

sealed class UIEvent {
  object CreateWallet: UIEvent()
}

data class UIState(
  val currentStep: CreateAccountStep = CreateAccountStep.CREATE_PASSWORD,
  val password: String = "",
  val passwordConfirmation: String = "",
  val showPassword: Boolean = false,
  val showPasswordConfirmation: Boolean = false,
  val isPasswordValid: Boolean = false,
  val isChecked: Boolean = false
)

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
  private val ethWalletRepository: EthWalletRepository
) : ViewModel() {
  private var _uiState = mutableStateOf(UIState())
  val uiState: State<UIState> = _uiState

  fun onEvent(event: UIEvent) {
    when(event) {
      is UIEvent.CreateWallet -> {

      }
      else -> { println("Undefined UIEvent") }
    }
  }

  fun setPassword(value: String) {
    _uiState.value = _uiState.value.copy(
      password = value
    )
  }

  fun setPasswordConfirmation(value: String) {
    _uiState.value = _uiState.value.copy(
      passwordConfirmation = value
    )
  }

  fun toggleShowPassword() {
    _uiState.value = _uiState.value.copy(
      showPassword = !_uiState.value.showPassword
    )
  }

  fun toggleShowPasswordConfirmation() {
    _uiState.value = _uiState.value.copy(
      showPasswordConfirmation = !_uiState.value.showPasswordConfirmation
    )
  }

  fun toggleIsChecked() {
    _uiState.value = _uiState.value.copy(
      isChecked = !_uiState.value.isChecked
    )
  }
}