package com.example.ethwalletapp.shared.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethwalletapp.shared.theme.Blue5

@Composable
fun GhostButton(
  onClick: () -> Unit,
  text: String,
  leadingIcon: @Composable (() -> Unit)? = null,
  trailingIcon: @Composable (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  OutlinedButton(
    onClick = onClick,
    shape = CircleShape,
    border = BorderStroke(1.dp, Blue5),
    colors = ButtonDefaults.outlinedButtonColors(
      contentColor =  Blue5,
      backgroundColor = Color.Transparent
    ),
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
        overflow = TextOverflow.Ellipsis
      )
      Spacer(Modifier.width(8.dp))
      trailingIcon?.invoke()
    }
  }
}

@Preview
@Composable
private fun GhostButtonPreview() {
  GhostButton(
    onClick = {},
    text = "Ghost"
  )
}