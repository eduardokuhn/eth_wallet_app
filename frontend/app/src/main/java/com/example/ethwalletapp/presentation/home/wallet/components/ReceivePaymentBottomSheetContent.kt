package com.example.ethwalletapp.presentation.home.wallet.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethwalletapp.shared.components.SecondaryButton
import com.example.ethwalletapp.shared.theme.Gray9
import com.example.ethwalletapp.shared.theme.Primary5
import org.kethereum.model.Address

@Composable
fun ReceivePaymentBottomSheetContent(
  address: Address?,
  copyToClipboard: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .padding(horizontal = 24.dp)
      .fillMaxWidth()
  ) {
    Spacer(Modifier.height(16.dp))
    Text(
      text = "Receive payment",
      fontSize = 18.sp,
      fontWeight = FontWeight.SemiBold,
      color = Color.White
    )
    Spacer(Modifier.height(38.dp))
    SecondaryButton(
      onClick = copyToClipboard,
      text = address?.hex ?: "?",
      trailingIcon = {
        Icon(
          Icons.Outlined.ContentCopy,
          contentDescription = "Copy address to clipboard",
          tint = Primary5
        )
      },
      textModifier = Modifier.weight(1f)
    )
    Spacer(Modifier.height(25.dp))
    Text(
      text = "Click on the address to copy it to the clipboard",
      fontSize = 13.sp,
      color = Gray9
    )
    Spacer(Modifier.height(42.dp))
  }
}

@Preview
@Composable
private fun ReceivePaymentBottomSheetContentPreview() {
  ReceivePaymentBottomSheetContent(
    address = Address("fedc65ce5964684df2eb0b4140ef0ca898b84e3fff635c1575dd991e2d1bd90b"),
    copyToClipboard = {}
  )
}