package com.example.ethwalletapp.shared.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethwalletapp.shared.theme.Gray18
import com.example.ethwalletapp.shared.theme.Gray21

@Composable
fun SecondaryButton(
  onClick: () -> Unit,
  text: String,
) {
  Button(
    onClick = onClick,
    colors = ButtonDefaults.buttonColors(backgroundColor = Gray21),
    shape = RoundedCornerShape(50.dp),
    contentPadding = PaddingValues(0.dp),
    modifier = Modifier
      .height(56.dp)
      .padding(horizontal = 24.dp)
      .fillMaxWidth()
  ) {
    Text(
      text = text,
      fontSize = 16.sp,
      color = Color.White,
    )
  }
}

@Preview
@Composable
fun SecondaryButtonPreview() {
  SecondaryButton(onClick = {}, text = "Enter")
}