package com.example.ethwalletapp.presentation.token_overview.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethwalletapp.data.models.TransactionEntry
import com.example.ethwalletapp.data.models.TransactionState
import com.example.ethwalletapp.shared.theme.Blue6
import com.example.ethwalletapp.shared.theme.Gray12
import com.example.ethwalletapp.shared.theme.Green6
import com.example.ethwalletapp.shared.theme.Red6

@Composable
fun TransactionListItem(
  isSentTransaction: Boolean,
  transaction: TransactionEntry,
  valueInUsd: String
) {
  Column(Modifier.padding(vertical = 16.dp)) {
    Text(
      text = transaction.transaction.creationEpochSecond.toString(),
      fontSize = 12.sp,
      color = Gray12
    )
    Spacer(Modifier.height(10.dp))
    Row(Modifier.fillMaxWidth()) {
      Icon(
        if (isSentTransaction) Icons.Outlined.Send else Icons.Outlined.Wallet,
        contentDescription = if (isSentTransaction) "Sent transaction"  else "Received transaction",
        tint = Color.White,
        modifier = Modifier.size(40.dp)
      )
      Spacer(Modifier.width(10.dp))
      Column {
        Row {
          Text(
            text = if (isSentTransaction) "Received ETH" else "Sent ETH",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
          )
          Spacer(
            Modifier
              .height(8.dp)
              .weight(1f))
          Text(
            text = "${transaction.transaction.value?.toBigDecimal() ?: "__.__"} ETH",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
          )
        }
        Spacer(Modifier.height(10.dp))
        Row(
          verticalAlignment = Alignment.Bottom
        ) {
          Text(
            text = transaction.state.toString(),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = when (transaction.state) {
              TransactionState.Confirmed -> Green6
              TransactionState.Failed -> Red6
              else -> Blue6
            }
          )
          Spacer(
            Modifier
              .height(8.dp)
              .weight(1f))
          Text(
            text = "$$valueInUsd",
            fontSize = 12.sp,
            color = Gray12
          )
        }
      }
    }
  }
}