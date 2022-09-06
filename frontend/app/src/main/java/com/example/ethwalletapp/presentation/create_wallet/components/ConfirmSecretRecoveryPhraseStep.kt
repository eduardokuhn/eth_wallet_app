package com.example.ethwalletapp.presentation.create_wallet.components

import PrimaryButton
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ethwalletapp.presentation.create_wallet.CreateWalletScreenUIState
import com.example.ethwalletapp.presentation.create_wallet.CreateWalletStep
import com.example.ethwalletapp.shared.theme.Gray22

@Composable
fun ConfirmSecretRecoveryPhraseStep(
  next: () -> Unit,
  uiState: CreateWalletScreenUIState,
  setSecretRecoveryPhraseConfirmation: (value: String) -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.fillMaxSize()
  ) {
    StepHeader(
      title = uiState.currentStep.title(),
      description = uiState.currentStep.description()
    )
    Spacer(Modifier.height(64.dp))
    BasicTextField(
      value = "",
      onValueChange = setSecretRecoveryPhraseConfirmation,
      textStyle = LocalTextStyle.current.copy(color = Color.White),
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .border(
          width = 1.dp,
          color = Gray22,
          shape = RoundedCornerShape(16.dp)
        )
        .background(
          color = Color.Transparent,
          shape = RoundedCornerShape(16.dp)
        )
    )
    Spacer(Modifier.height(110.dp))
    PrimaryButton(
      onClick = next,
      text = "Next",
      disabled = !uiState.isSRPConfirmed,
      modifier = Modifier.fillMaxWidth()
    )
    Spacer(Modifier.height(42.dp))
  }
}

@Preview
@Composable
private fun ConfirmSecretRecoveryPhraseStepPreview() {
  ConfirmSecretRecoveryPhraseStep(
    next = {},
    uiState = CreateWalletScreenUIState().copy(currentStep = CreateWalletStep.ConfirmSecretRecoveryPhrase),
    setSecretRecoveryPhraseConfirmation = {}
  )
}