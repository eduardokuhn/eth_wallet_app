package com.example.ethwalletapp.presentation.create_wallet.components

import PrimaryButton
import TextInput
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethwalletapp.presentation.create_wallet.CreateWalletScreenUIState
import com.example.ethwalletapp.shared.theme.Gray22
import com.example.ethwalletapp.shared.theme.Primary5

@Composable
fun CreatePasswordStep(
  createPassword: () -> Unit,
  uiState: CreateWalletScreenUIState,
  setPassword: (value: String) -> Unit,
  toggleShowPassword: () -> Unit,
  setPasswordConfirmation: (value: String) -> Unit,
  toggleShowPasswordConfirmation: () -> Unit,
  toggleIsChecked: (Boolean) -> Unit,
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.fillMaxSize()
  ) {
    StepHeader(
      title = uiState.currentStep.title(),
      description = uiState.currentStep.description()
    )
    TextInput(
      value = uiState.password,
      onValueChange = setPassword,
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
      visualTransformation =
      if (uiState.showPassword) VisualTransformation.None
      else PasswordVisualTransformation(),
      label = "Password",
      trailingIcon = {
        IconButton(
          onClick = toggleShowPassword,
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
      hasError = uiState.hasError,
      helperText = uiState.passwordHelperText
    )
    Spacer(modifier = Modifier.height(24.dp))
    TextInput(
      value = uiState.passwordConfirmation,
      onValueChange = setPasswordConfirmation,
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
      visualTransformation =
      if (uiState.showPasswordConfirmation) VisualTransformation.None
      else PasswordVisualTransformation(),
      label = "Password Confirmation",
      trailingIcon = {
        IconButton(
          onClick = toggleShowPasswordConfirmation,
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
      hasError = uiState.hasError,
      helperText = uiState.passwordHelperText
    )
    Spacer(modifier = Modifier.height(40.dp))
    Row(
      verticalAlignment = Alignment.Bottom
    ) {
      Checkbox(
        checked = uiState.isChecked,
        onCheckedChange = toggleIsChecked,
        colors = CheckboxDefaults.colors(
          checkedColor = Primary5,
          uncheckedColor = Gray22
        )
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "I understand that anybody can recover this password for me.",
        fontSize = 14.sp,
        color = Color.White
      )
    }
    Spacer(modifier = Modifier.weight(1f))
    PrimaryButton(
      onClick = createPassword,
      text = "Create Password",
      disabled = !uiState.isChecked,
      modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(42.dp))
  }
}

@Preview
@Composable
private fun CreateWalletStepPreview() {
  CreatePasswordStep(
    createPassword = {},
    uiState = CreateWalletScreenUIState(),
    setPassword = {},
    toggleShowPassword = {},
    setPasswordConfirmation = {},
    toggleShowPasswordConfirmation = {},
    toggleIsChecked = {}
  )
}