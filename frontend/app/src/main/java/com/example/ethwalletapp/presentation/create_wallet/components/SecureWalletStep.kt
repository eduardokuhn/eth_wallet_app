package com.example.ethwalletapp.presentation.create_wallet.components

import PrimaryButton
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethwalletapp.presentation.create_wallet.CreateWalletScreenUIState
import com.example.ethwalletapp.shared.components.SecondaryButton
import com.example.ethwalletapp.shared.theme.Gray22
import com.example.ethwalletapp.shared.theme.Gray9
import com.example.ethwalletapp.shared.theme.Primary5
import com.example.ethwalletapp.presentation.create_wallet.CreateWalletStep

@Composable
fun SecureWalletStep(
  next: () -> Unit,
  uiState: CreateWalletScreenUIState,
  toggleShowSecretRecoveryPhrase: () -> Unit
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
    Box(
      modifier = Modifier
        .weight(1f)
        .background(color = Gray22)
        .border(
          width = 1.dp,
          color = Color.White.copy(alpha = 0.06f),
          shape = RoundedCornerShape(8.dp)
        )
    ) {
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .blur(if (!uiState.showSecretRecoveryPhrase) 30.dp else 0.dp)
          .padding(horizontal = 24.dp)
          .fillMaxSize()
      ) {
        Text(
          text = uiState.secretRecoveryPhrase,
          fontSize = 14.sp,
          color = Color.White
        )
      }
      if(!uiState.showSecretRecoveryPhrase) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center,
          modifier = Modifier.fillMaxSize()
        ) {
          Text(
            text = "Tap to reveal your secret recovery phrase",
            fontSize = 14.sp,
            color = Color.White
          )
          Spacer(Modifier.height(12.dp))
          Text(
            text = "Make sure no one is watching your screen.",
            fontSize = 14.sp,
            color = Gray9
          )
          Spacer(Modifier.height(40.dp))
          SecondaryButton(
            onClick = toggleShowSecretRecoveryPhrase,
            text = "View",
            leadingIcon = {
              Icon(
                if (uiState.showSecretRecoveryPhrase) Icons.Outlined.VisibilityOff
                else Icons.Outlined.Visibility,
                contentDescription =
                if (uiState.showSecretRecoveryPhrase) "Show secret recovery phrase"
                else "Hide secret recovery phrase",
                tint = Primary5
              )
            },
          )
        }
      }
    }
    Spacer(Modifier.height(110.dp))
    PrimaryButton(
      onClick = next,
      text = "Next",
      disabled = !uiState.showSecretRecoveryPhrase,
      modifier = Modifier.fillMaxWidth()
    )
    Spacer(Modifier.height(42.dp))
  }
}

@Preview
@Composable
private fun SecureWalletStepPreview() {
  SecureWalletStep(
    next = {},
    uiState = CreateWalletScreenUIState().copy(currentStep = CreateWalletStep.SecureWallet),
    toggleShowSecretRecoveryPhrase = {}
  )
}