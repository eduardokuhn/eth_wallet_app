package com.example.ethwalletapp.presentation.create_wallet.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethwalletapp.presentation.create_wallet.CreateAccountStep
import com.example.ethwalletapp.shared.components.SecondaryButton
import com.example.ethwalletapp.shared.theme.Gradient07
import com.example.ethwalletapp.shared.theme.Gray9
import com.example.ethwalletapp.shared.theme.Mask

@Composable
fun SecureWalletStep(

) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.fillMaxSize()
  ) {
    StepHeader(title = "Title", description = "Description")
    Spacer(Modifier.height(64.dp))
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(Mask)
        .border(
          width = 1.dp,
          color = Color.White.copy(alpha = 0.06f),
          shape = RoundedCornerShape(8.dp)
        )
    ) {
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
          color = Color.White
        )
        Spacer(Modifier.height(40.dp))
        SecondaryButton(
          onClick = {},
          text = "View"
        )
      }
    }
  }
}

@Preview
@Composable
private fun SecureWalletStepPreview() {
  SecureWalletStep()
}