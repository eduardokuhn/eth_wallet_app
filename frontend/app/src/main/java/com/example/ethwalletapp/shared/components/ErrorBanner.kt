package com.example.ethwalletapp.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethwalletapp.shared.theme.*

@Composable
fun ErrorBanner(
  title: String,
  description: String
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .background(Red8.copy(alpha = 0.1f))
      .fillMaxWidth()
      .padding(16.dp)
  ) {
    Icon(
      Icons.Outlined.ErrorOutline,
      contentDescription = "Error",
      tint = Red7,
      modifier = Modifier.size(40.dp)
    )
    Spacer(Modifier.width(15.dp))
    Column() {
      Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White
      )
      Spacer(Modifier.height(6.dp))
      Text(
        text = description,
        fontSize = 14.sp,
        color = Gray9
      )
    }
  }
  /*
  OLD
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .background(Brush.horizontalGradient(Gradient08))
      .fillMaxWidth()
      .padding(24.dp)
  ) {
    Icon(
      Icons.Outlined.Error,
      contentDescription = "Error",
      tint = Red6
    )
    Spacer(Modifier.width(24.dp))
    Column() {
      Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = Red5
      )
      Text(
        text = description,
        fontSize = 16.sp,
        color = Gray9
      )
    }
  }
   */
}

@Preview
@Composable
private fun ErrorBannerPreview() {
  ErrorBanner(
    title = "Error! Retry it again",
    description = "There was an error loading data"
  )
}