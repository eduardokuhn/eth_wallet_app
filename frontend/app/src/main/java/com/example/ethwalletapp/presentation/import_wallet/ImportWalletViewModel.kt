package com.example.ethwalletapp.presentation.import_wallet

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ethwalletapp.data.services.IAccountService
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
  val isLoading: Boolean = false
)

interface IImportWalletViewModel {
  var uiState: MutableState<ImportWalletScreenUIState>
  fun setSecretRecoveryPhrase(value: String)
  fun toggleShowSecretRecoveryPhrase()
  fun setPassword(value: String)
  fun setPasswordConfirmation(value: String)
  fun toggleShowPassword()
  fun toggleShowPasswordConfirmation()
  suspend fun importWallet(): Boolean
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
  override suspend fun importWallet(): Boolean { return true }
}

@HiltViewModel
class ImportWalletViewModel @Inject constructor(
  private val accountService: IAccountService
): ViewModel(), IImportWalletViewModel {
  override var uiState = mutableStateOf(ImportWalletScreenUIState())

  override fun setSecretRecoveryPhrase(value: String) {
    val valid = accountService.validateSecretRecoveryPhrase(value)
    uiState.value = uiState.value.copy(
      secretRecoveryPhrase = value,
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

  override suspend fun importWallet(): Boolean {
    uiState.value = uiState.value.copy(
      isLoading = uiState.value.isSRPValid && uiState.value.isPasswordValid,
      hasSRPError = !uiState.value.isSRPValid,
      srpHelperText =
        if (!uiState.value.isSRPValid) "Invalid secret recovery phrase"
        else null,
      hasPasswordError = !uiState.value.isPasswordValid,
      passwordHelperText =
        if (!uiState.value.isPasswordValid) "Must be at least 8 characters"
        else "Password does not match the requirements",
    )
    return if (uiState.value.isSRPValid && uiState.value.isPasswordValid) {
      accountService.importMasterAccount(uiState.value.secretRecoveryPhrase, uiState.value.password)
      uiState.value = uiState.value.copy(isLoading = false)
      true
    } else false
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