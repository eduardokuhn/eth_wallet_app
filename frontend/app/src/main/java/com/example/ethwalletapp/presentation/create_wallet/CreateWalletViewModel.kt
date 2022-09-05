package com.example.ethwalletapp.presentation.create_wallet

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ethwalletapp.data.services.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class CreateAccountStep {
  object CreateWallet: CreateAccountStep()
  object SecureWallet: CreateAccountStep()
  object ConfirmSecretRecoveryPhrase: CreateAccountStep()

  fun title(): String {
    return when(this) {
      CreateWallet -> "Create Wallet"
      SecureWallet -> "Secure Your Wallet"
      ConfirmSecretRecoveryPhrase -> "Confirm Secret Recovery Phrase"
    }
  }

  fun description(): String {
    return when(this) {
      CreateWallet ->
        "This password will unlock your wallet only on this service."
      SecureWallet ->
        "This is your secret recovery phrase. Write it down on a paper and keep it in a safe place. You'll be asked to re-enter this phrase (in order) on the next step."
      ConfirmSecretRecoveryPhrase ->
        "Type your secret recovery phrase."
    }
  }

  fun index(): Int {
    return when(this) {
      CreateWallet -> 0
      SecureWallet -> 1
      ConfirmSecretRecoveryPhrase -> 2
    }
  }
}

data class UIState(
  val currentStep: CreateAccountStep = CreateAccountStep.CreateWallet,
  val password: String = "",
  val passwordConfirmation: String = "",
  val showPassword: Boolean = false,
  val showPasswordConfirmation: Boolean = false,
  val isPasswordValid: Boolean = false,
  val hasError: Boolean = false,
  val passwordHelperText: String = "Must be at least 8 characters",
  val isChecked: Boolean = false
)

interface ICreateAccountViewModel {
  var uiState: MutableState<UIState>
  fun setPassword(value: String)
  fun setPasswordConfirmation(value: String)
  fun toggleShowPassword()
  fun toggleShowPasswordConfirmation()
  fun toggleIsChecked(value: Boolean)
  fun createWallet(): Boolean
  fun setCurrentStep(step: CreateAccountStep)
}

@HiltViewModel
class CreateWalletViewModel @Inject constructor(
  private val accountService: AccountService
) : ViewModel(), ICreateAccountViewModel {
  override var uiState = mutableStateOf(UIState())

  override fun setPassword(value: String) {
    uiState.value = uiState.value.copy(password = value)
  }

  override fun setPasswordConfirmation(value: String) {
    uiState.value = uiState.value.copy(passwordConfirmation = value)
    validatePassword()
  }

  override fun toggleShowPassword() {
    uiState.value = uiState.value.copy(showPassword = !uiState.value.showPassword)
  }

  override fun toggleShowPasswordConfirmation() {
    uiState.value = uiState.value.copy(
      showPasswordConfirmation = !uiState.value.showPasswordConfirmation
    )
  }

  override fun toggleIsChecked(value: Boolean) {
    uiState.value = uiState.value.copy(isChecked = value)
  }

  override fun createWallet(): Boolean {
    return if (uiState.value.isPasswordValid) {
      uiState.value = uiState.value.copy(
        hasError = false,
        passwordHelperText = "Must be at least 8 characters"
      )
      uiState.value.isChecked
    } else {
      uiState.value = uiState.value.copy(
        hasError = true,
        passwordHelperText = "Password does not match the requirements"
      )
      false
    }
  }

  override fun setCurrentStep(step: CreateAccountStep) {
    uiState.value = uiState.value.copy(currentStep = step)
  }

  private fun validatePassword() {
    if (
      (uiState.value.password == uiState.value.passwordConfirmation) &&
      (uiState.value.password.length >= 8)
    )
      uiState.value = uiState.value.copy(isPasswordValid = true)
    else
      uiState.value = uiState.value.copy(isPasswordValid = false)
  }
}

class CreateWalletViewModelMock: ICreateAccountViewModel {
  override var uiState: MutableState<UIState>
    get() = mutableStateOf(UIState())
    set(value) {}
  override fun setPassword(value: String) {}
  override fun setPasswordConfirmation(value: String) {}
  override fun toggleShowPassword() {}
  override fun toggleShowPasswordConfirmation() {}
  override fun toggleIsChecked(value: Boolean) {}
  override fun createWallet(): Boolean { return true }
  override fun setCurrentStep(step: CreateAccountStep) {}
}