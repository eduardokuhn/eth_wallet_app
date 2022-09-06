package com.example.ethwalletapp.presentation.import_wallet

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ethwalletapp.data.services.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class ImportWalletScreenUIState(
  val secretRecoveryPhrase: String = "",
  val showSecretRecoveryPhrase: Boolean = false,
  val isSRPValid: Boolean = false,
  val hasSRPError: Boolean = false,
  val srpHelperText: String? = null,
  val password: String = "",
  val passwordConfirmation: String = "",
  val showPassword: Boolean = false,
  val showPasswordConfirmation: Boolean = false,
  val isPasswordValid: Boolean = false,
  val hasPasswordError: Boolean = false,
  val passwordHelperText: String = "Must be at least 8 characters",
)

interface IImportWalletViewModel {
  var uiState: MutableState<ImportWalletScreenUIState>
  fun setSecretRecoveryPhrase(value: String)
  fun toggleShowSecretRecoveryPhrase()
  fun setPassword(value: String)
  fun setPasswordConfirmation(value: String)
  fun toggleShowPassword()
  fun toggleShowPasswordConfirmation()
  fun importWallet()
}

@HiltViewModel
class ImportWalletViewModel @Inject constructor(
  private val accountService: AccountService
): ViewModel(), IImportWalletViewModel {
  override var uiState = mutableStateOf(ImportWalletScreenUIState())

  override fun setSecretRecoveryPhrase(value: String) {
    val valid = accountService.validateSecretRecoveryPhrase(value.trim())
    uiState.value = uiState.value.copy(
      secretRecoveryPhrase = value.trim(),
      isSRPValid = valid
    )
  }

  override fun toggleShowSecretRecoveryPhrase() {
    uiState.value = uiState.value.copy(showSecretRecoveryPhrase = !uiState.value.showSecretRecoveryPhrase)
  }

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
    uiState.value = uiState.value.copy(showPasswordConfirmation = !uiState.value.showPasswordConfirmation)
  }

  override fun importWallet() {
    TODO("Not yet implemented")
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

class ImportWalletViewModelMock: IImportWalletViewModel {
  override var uiState: MutableState<ImportWalletScreenUIState>
    get() = mutableStateOf(ImportWalletScreenUIState())
    set(value) { print(value) }
  override fun setSecretRecoveryPhrase(value: String) {}
  override fun toggleShowSecretRecoveryPhrase() {}
  override fun setPassword(value: String) {}
  override fun setPasswordConfirmation(value: String) {}
  override fun toggleShowPassword() {}
  override fun toggleShowPasswordConfirmation() {}
  override fun importWallet() {}
}