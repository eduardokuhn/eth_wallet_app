package com.example.ethwalletapp.presentation.send_payment.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethwalletapp.data.models.AccountEntry
import com.example.ethwalletapp.data.models.BalanceEntry
import com.example.ethwalletapp.shared.theme.Gray12
import com.example.ethwalletapp.shared.theme.Green5
import com.example.ethwalletapp.shared.utils.weiToEther

@Composable
fun AccountInput(
  selectedAccount: AccountEntry?,
  selectedAccountBalance: BalanceEntry?,
  showAddress: Boolean = false,
  onClick: (() -> Unit)? = null,
  trailingIcon: (@Composable () -> Unit)? = null
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .clickable { onClick?.invoke() }
      .fillMaxWidth()
  ) {
    Box(
      modifier = Modifier
        .size(40.dp)
        .background(Green5, CircleShape)
    ) {
      Text(
        text = if (selectedAccount != null) "${selectedAccount.addressIndex + 1}" else "?",
        fontSize = 18.sp,
        color = Color.White,
        modifier = Modifier.align(Alignment.Center)
      )
    }
    Spacer(Modifier.width(16.dp))
    Column(Modifier.weight(1f)) {
      Text(
        text = selectedAccount?.name ?: "?",
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White
      )
      Spacer(Modifier.height(6.dp))
      Text(
        text =
        if (showAddress && selectedAccount != null) selectedAccount.address.hex
        else {
          if (selectedAccountBalance != null) "Balance: ${selectedAccountBalance.balance.weiToEther()} ETH"
          else "Balance: __.__ ETH"
        },
        fontSize = 12.sp,
        color = Gray12,
        maxLines = 1
      )
    }
    trailingIcon?.invoke()
  }
}