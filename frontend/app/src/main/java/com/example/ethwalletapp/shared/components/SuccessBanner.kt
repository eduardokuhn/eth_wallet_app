package com.example.ethwalletapp.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethwalletapp.shared.theme.Gray9
import com.example.ethwalletapp.shared.theme.Green7
import com.example.ethwalletapp.shared.theme.Green8

@Composable
fun SuccessBanner(
  title: String,
  description: String
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .background(Green8.copy(alpha = 0.1f))
      .fillMaxWidth()
      .padding(16.dp)
  ) {
    Icon(
      Icons.Outlined.AccessTime,
      contentDescription = "Success",
      tint = Green7,
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
}

@Preview
@Composable
private fun SuccessBannerPreview() {
  SuccessBanner(
    title = "Transaction Submitted",
    description = "Waiting for confirmation"
  )
}