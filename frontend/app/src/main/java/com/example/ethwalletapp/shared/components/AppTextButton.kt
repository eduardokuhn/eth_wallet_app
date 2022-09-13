package com.example.ethwalletapp.shared.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethwalletapp.shared.theme.Blue5

@Composable
fun AppTextButton(
  onClick: () -> Unit,
  text: String,
  leadingIcon: @Composable (() -> Unit)? = null,
  trailingIcon: @Composable (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  TextButton(
    onClick = onClick,
    modifier = modifier
      .height(56.dp)
  ) {
    Row(Modifier.padding(horizontal = 16.dp)) {
      leadingIcon?.invoke()
      if (leadingIcon != null) Spacer(Modifier.width(8.dp))
      Text(
        text = text,
        fontSize = 16.sp,
        color = Blue5,
        overflow = TextOverflow.Ellipsis
      )
      if (trailingIcon != null) Spacer(Modifier.width(8.dp))
      trailingIcon?.invoke()
    }
  }
}

@Preview
@Composable
private fun TextButtonPreview() {
  AppTextButton(
    onClick = {},
    text = "Text"
  )
}