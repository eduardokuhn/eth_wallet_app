package com.example.ethwalletapp.presentation.send_payment.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConfirmView(
  onBack: () -> Unit,
  onClose: () -> Unit,
) {
  Column(Modifier.fillMaxSize()) {
    Spacer(Modifier.height(8.dp))
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp)
    ) {
      IconButton(
        onClick = onBack,
        modifier = Modifier.align(Alignment.CenterStart)
      ) {
        Icon(
          Icons.Outlined.KeyboardArrowLeft,
          contentDescription = "Back to amount",
          tint = Color.White
        )
      }
      Text(
        text = "Confirm",
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White,
        modifier = Modifier.align(Alignment.Center)
      )
      IconButton(
        onClick =  onClose,
        modifier = Modifier.align(Alignment.CenterEnd)
      ) {
        Icon(
          Icons.Outlined.Close,
          contentDescription = "Close screen",
          tint = Color.White
        )
      }
    }
    Spacer(Modifier.height(18.dp))
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier
        .padding(horizontal = 24.dp)
        .fillMaxSize()
    ) {
      Text(text = "Amount")
    }
  }
}

@Preview
@Composable
private fun ConfirmViewPreview() {
  ConfirmView(
    onBack = {},
    onClose = {}
  )
}