package com.example.ethwalletapp.shared.components

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethwalletapp.shared.theme.Gray21
import com.example.ethwalletapp.shared.theme.Primary5

@Composable
fun SecondaryButton(
  onClick: () -> Unit,
  text: String,
  leadingIcon: @Composable (() -> Unit)? = null,
  trailingIcon: @Composable (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  Button(
    onClick = onClick,
    colors = ButtonDefaults.buttonColors(backgroundColor = Gray21),
    shape = RoundedCornerShape(50.dp),
    contentPadding = PaddingValues(0.dp),
    modifier = modifier
      .height(56.dp)
  ) {
    Row(Modifier.padding(horizontal = 16.dp)) {
      leadingIcon?.invoke()
      Spacer(Modifier.width(8.dp))
      Text(
        text = text,
        fontSize = 16.sp,
        color = Color.White,
      )
      Spacer(Modifier.width(8.dp))
      trailingIcon?.invoke()
    }
  }
}

@Preview
@Composable
fun SecondaryButtonPreview() {
  SecondaryButton(
    onClick = {},
    text = "Enter",
    leadingIcon = {
      Icon(
        Icons.Outlined.VisibilityOff,
        contentDescription = "Show secret recovery phrase",
        tint = Primary5
      )
    }
  )
}