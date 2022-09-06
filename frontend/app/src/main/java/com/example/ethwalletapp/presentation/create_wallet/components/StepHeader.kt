package com.example.ethwalletapp.presentation.create_wallet.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethwalletapp.presentation.create_wallet.CreateWalletStep
import com.example.ethwalletapp.shared.theme.Gradient07
import com.example.ethwalletapp.shared.theme.Gray9

@Composable
fun StepHeader(
  title: String,
  description: String
) {
  Spacer(modifier = Modifier.height(40.dp))
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    Text(
      text = title,
      fontSize = 16.sp,
      modifier = Modifier
        .graphicsLayer(alpha = 0.99f)
        .drawWithCache {
          val brush = Brush.horizontalGradient(Gradient07)
          onDrawWithContent {
            drawContent()
            drawRect(brush, blendMode = BlendMode.SrcAtop)
          }
        }
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
      text = description,
      fontSize = 14.sp,
      color = Gray9
    )
  }
  Spacer(modifier = Modifier.height(40.dp))
}

@Preview
@Composable
private fun StepHeaderPreview() {
  val step = CreateWalletStep.CreatePassword
  StepHeader(title = step.title(), description = step.description())
}