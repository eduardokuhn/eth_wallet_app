package com.example.ethwalletapp.presentation.create_wallet

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ethwalletapp.data.services.IAccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class CreateWalletStep {
  object CreatePassword: CreateWalletStep()
  object SecureWallet: CreateWalletStep()
  object ConfirmSecretRecoveryPhrase: CreateWalletStep()

  fun title(): String {
    return when(this) {
      CreatePassword -> "Create Password"
      SecureWallet -> "Secure Your Wallet"
      ConfirmSecretRecoveryPhrase -> "Confirm Secret Recovery Phrase"
    }
  }

  fun description(): String {
    return when(this) {
      CreatePassword ->
        "This password will unlock your wallet only on this service."
      SecureWallet ->
        "This is your secret recovery phrase. Write it down on a paper and keep it in a safe place. You'll be asked to re-enter this phrase (in order) on the next step."
      ConfirmSecretRecoveryPhrase ->
        "Type your secret recovery phrase."
    }
  }

  fun index(): Int {
    return when(this) {
      CreatePassword -> 0
      SecureWallet -> 1
      ConfirmSecretRecoveryPhrase -> 2
    }
  }
}

data class CreateWalletScreenUIState(
  val currentStep: CreateWalletStep = CreateWalletStep.CreatePassword,
  val password: String = "",
  val passwordConfirmation: String = "",
  val showPassword: Boolean = false,
  val showPasswordConfirmation: Boolean = false,
  val isPasswordValid: Boolean = false,
  val hasError: Boolean = false,
  val passwordHelperText: String = "Must be at least 8 characters",
  val isChecked: Boolean = false,
  val secretRecoveryPhrase: String = "",
  val secretRecoveryPhraseConfirmation: String = "",
  val showSecretRecoveryPhrase: Boolean = false,
  val isSRPConfirmed: Boolean = false,
  val isLoading: Boolean = false,
)

interface ICreateAccountViewModel {
  var uiState: MutableState<CreateWalletScreenUIState>
  fun setPassword(value: String)
  fun setPasswordConfirmation(value: String)
  fun toggleShowPassword()
  fun toggleShowPasswordConfirmation()
  fun toggleIsChecked(value: Boolean)
  fun createPassword(): Boolean
  suspend fun createWallet(): Boolean
  fun setCurrentStep(step: CreateWalletStep)
  fun toggleShowSecretRecoveryPhrase()
  fun setSecretRecoveryPhraseConfirmation(value: String)
}

class CreateWalletViewModelMock: ICreateAccountViewModel {
  override var uiState: MutableState<CreateWalletScreenUIState>
    get() = mutableStateOf(CreateWalletScreenUIState())
    set(value) { print(value) }
  override fun setPassword(value: String) {}
  override fun setPasswordConfirmation(value: String) {}
  override fun toggleShowPassword() {}
  override fun toggleShowPasswordConfirmation() {}
  override fun toggleIsChecked(value: Boolean) {}
  override fun createPassword(): Boolean { return true }
  override suspend fun createWallet(): Boolean { return true }
  override fun setCurrentStep(step: CreateWalletStep) {}
  override fun toggleShowSecretRecoveryPhrase() {}
  override fun setSecretRecoveryPhraseConfirmation(value: String) {}
}

@HiltViewModel
class CreateWalletViewModel @Inject constructor(
  private val accountService: IAccountService
) : ViewModel(), ICreateAccountViewModel {
  override var uiState = mutableStateOf(CreateWalletScreenUIState())

  override fun setPassword(value: String) {
    uiState.value = uiState.value.copy(password = value)
    validatePassword()
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

  override fun createPassword(): Boolean {
    if (uiState.value.isPasswordValid && uiState.value.isChecked) {
      val srp = accountService.generateSecretRecoveryPhrase()
      uiState.value = uiState.value.copy(
        hasError = false,
        passwordHelperText = "Must be at least 8 characters",
        secretRecoveryPhrase = srp
      )
      return true
    } else {
      uiState.value = uiState.value.copy(
        hasError = true,
        passwordHelperText = "Password does not match the requirements",
        secretRecoveryPhrase = ""
      )
      return false
    }
  }

  override suspend fun createWallet(): Boolean {
    return if (uiState.value.isSRPConfirmed) {
      uiState.value = uiState.value.copy(isLoading = true)
      accountService.createMasterAccount(uiState.value.secretRecoveryPhrase, uiState.value.password)
      uiState.value = uiState.value.copy(isLoading = false)

      true
    } else false
  }

  override fun setCurrentStep(step: CreateWalletStep) {
    uiState.value = uiState.value.copy(currentStep = step)
  }

  override fun toggleShowSecretRecoveryPhrase() {
    uiState.value = uiState.value.copy(showSecretRecoveryPhrase = !uiState.value.showSecretRecoveryPhrase)
  }

  override fun setSecretRecoveryPhraseConfirmation(value: String) {
    uiState.value = uiState.value.copy(secretRecoveryPhraseConfirmation = value)
    confirmSecretRecoveryPhrase()
  }

  private fun confirmSecretRecoveryPhrase() {
    if (uiState.value.secretRecoveryPhrase == uiState.value.secretRecoveryPhraseConfirmation)
      uiState.value = uiState.value.copy(isSRPConfirmed = true)
    else
      uiState.value = uiState.value.copy(isSRPConfirmed = false)
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