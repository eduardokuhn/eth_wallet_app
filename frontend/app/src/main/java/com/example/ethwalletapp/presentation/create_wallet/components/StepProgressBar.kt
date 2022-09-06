package com.example.ethwalletapp.presentation.create_wallet.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.ethwalletapp.presentation.create_wallet.CreateWalletStep
import com.example.ethwalletapp.shared.theme.Gray21
import com.example.ethwalletapp.shared.theme.Primary5

@Composable
fun StepProgressBar(
  currentStep: CreateWalletStep,
  modifier: Modifier = Modifier,
) {
  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
  ) {
    val stepCount = 3

    Canvas(modifier = Modifier
      .fillMaxWidth()
      .zIndex(-1f)
    ) {
      val height = drawContext.size.height
      val width = drawContext.size.width

      val yOffset = height / 2
      val stepWidth = width / stepCount

      var startOffset = stepWidth / 2
      var endOffset = startOffset

      repeat(stepCount - 1) { index ->
        endOffset += stepWidth
        drawLine(
          start = Offset(startOffset, yOffset),
          end = Offset(endOffset, yOffset),
          color = if (index < currentStep.index()) Primary5 else Gray21,
          strokeWidth = 1.dp.toPx()
        )
        startOffset = endOffset
      }
    }
    Row(
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.fillMaxWidth()
    ) {
      repeat(stepCount) { index ->
        Box(contentAlignment = Alignment.Center) {
          StepDot(index = index, currentStep = currentStep)
        }
      }
    }
  }
}

@Preview
@Composable
private fun CreateWalletStepProgressBarPreview() {
  StepProgressBar(currentStep = CreateWalletStep.CreatePassword)
}

@Preview
@Composable
private fun SecureWalletStepProgressBarPreview() {
  StepProgressBar(currentStep = CreateWalletStep.SecureWallet)
}

@Preview
@Composable
private fun ConfirmStepProgressBarPreview3() {
  StepProgressBar(currentStep = CreateWalletStep.ConfirmSecretRecoveryPhrase)
}

@Composable
fun StepDot(index: Int, currentStep: CreateWalletStep) {
  Canvas(
    modifier = Modifier
      .size(8.dp),
    onDraw = {
      drawCircle(color = if (index <= currentStep.index()) Primary5 else Gray21)
    }
  )
}