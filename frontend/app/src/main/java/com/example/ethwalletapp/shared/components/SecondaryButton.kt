package com.example.ethwalletapp.shared.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
  modifier: Modifier = Modifier,
  textModifier: Modifier = Modifier
) {
  Button(
    onClick = onClick,
    colors = ButtonDefaults.buttonColors(backgroundColor = Gray21),
    shape = RoundedCornerShape(50.dp),
    contentPadding = PaddingValues(0.dp),
    modifier = modifier
      .height(56.dp)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(horizontal = 16.dp)
    ) {
      leadingIcon?.invoke()
      if (leadingIcon != null) Spacer(Modifier.width(8.dp))
      Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        modifier = textModifier
      )
      if (trailingIcon != null) Spacer(Modifier.width(8.dp))
      trailingIcon?.invoke()
    }
  }
}

@Preview
@Composable
fun SecondaryButtonPreview() {
  SecondaryButton(
    onClick = {},
    text = "fedc65ce5964684df2eb0b4140ef0ca898b84e3fff635c1575dd991e2d1bd90b",
    trailingIcon = {
      Icon(
        Icons.Outlined.VisibilityOff,
        contentDescription = "Show secret recovery phrase",
        tint = Primary5
      )
    }
  )
}