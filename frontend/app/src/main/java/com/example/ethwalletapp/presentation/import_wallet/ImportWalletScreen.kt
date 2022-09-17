package com.example.ethwalletapp.presentation.import_wallet

import PrimaryButton
import TextInput
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ethwalletapp.shared.navigation.Screen
import com.example.ethwalletapp.shared.theme.Gray24
import kotlinx.coroutines.launch

@Composable
fun ImportWalletScreen(
  navController: NavController?,
  viewModel: IImportWalletViewModel
) {
  val uiState = viewModel.uiState.value
  val scope = rememberCoroutineScope()

  Scaffold(
    backgroundColor = Gray24
  ) { padding ->
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier
        .padding(padding)
        .fillMaxSize()
        .padding(horizontal = 24.dp)
    ) {
      Spacer(modifier = Modifier.height(44.dp))
      Box(Modifier.fillMaxWidth()) {
        IconButton(
          onClick = { navController?.navigate(Screen.StartScreen.route) },
          modifier = Modifier.align(Alignment.CenterStart)
        ) {
          Icon(
            Icons.Filled.KeyboardArrowLeft,
            contentDescription = "Back to start screen",
            tint = Color.White
          )
        }
        Text(
          text = "Import From SRP",
          fontWeight = FontWeight.SemiBold,
          color = Color.White,
          modifier = Modifier.align(Alignment.Center)
        )
      }
      Spacer(modifier = Modifier.height(40.dp))
      TextInput(
        value = uiState.secretRecoveryPhrase,
        onValueChange = viewModel::setSecretRecoveryPhrase,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation =
        if (uiState.showSecretRecoveryPhrase) VisualTransformation.None
        else PasswordVisualTransformation(),
        label = "Secret Recovery Phrase",
        trailingIcon = {
          IconButton(
            onClick = viewModel::toggleShowSecretRecoveryPhrase,
          ) {
            Icon(
              if (uiState.showSecretRecoveryPhrase) Icons.Outlined.VisibilityOff
              else Icons.Outlined.Visibility,
              contentDescription =
              if (uiState.showSecretRecoveryPhrase) "Hide secret recovery phrase"
              else "Show secret recovery phrase",
              tint = Color.White
            )
          }
        },
        hasError = uiState.hasSRPError,
        helperText = uiState.srpHelperText
      )
      Spacer(modifier = Modifier.height(24.dp))
      TextInput(
        value = uiState.password,
        onValueChange = viewModel::setPassword,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation =
          if (uiState.showPassword) VisualTransformation.None
          else PasswordVisualTransformation(),
        label = "Password",
        trailingIcon = {
          IconButton(
            onClick = viewModel::toggleShowPassword,
          ) {
            Icon(
              if (uiState.showPassword) Icons.Outlined.VisibilityOff
              else Icons.Outlined.Visibility,
              contentDescription =
                if (uiState.showPassword) "Hide password"
                else "Show password",
              tint = Color.White
            )
          }
        },
        hasError = uiState.hasPasswordError,
        helperText = uiState.passwordHelperText
      )
      Spacer(modifier = Modifier.height(24.dp))
      TextInput(
        value = uiState.passwordConfirmation,
        onValueChange = viewModel::setPasswordConfirmation,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation =
          if (uiState.showPasswordConfirmation) VisualTransformation.None
          else PasswordVisualTransformation(),
        label = "Password Confirmation",
        trailingIcon = {
          IconButton(
            onClick = viewModel::toggleShowPasswordConfirmation,
          ) {
            Icon(
              if (uiState.showPasswordConfirmation) Icons.Outlined.VisibilityOff
              else Icons.Outlined.Visibility,
              contentDescription =
                if (uiState.showPasswordConfirmation) "Hide password"
                else "Show password",
              tint = Color.White
            )
          }
        },
        hasError = uiState.hasPasswordError,
        helperText = uiState.passwordHelperText
      )
      Spacer(Modifier.weight(1f))
      PrimaryButton(
        onClick = {
          scope.launch {
            val success = viewModel.importWallet()
            if (success)
              navController?.navigate(Screen.HomeScreen.route)
          }
        },
        text = "Import",
        disabled = !(uiState.password.isNotEmpty() && uiState.secretRecoveryPhrase.isNotEmpty()),
        isLoading = uiState.isLoading,
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(Modifier.height(42.dp))
    }
  }
}

@Preview
@Composable
private fun ImportWalletScreenPreview() {
  val viewModelMock = ImportWalletViewModelMock()
  ImportWalletScreen(null, viewModelMock)
}
