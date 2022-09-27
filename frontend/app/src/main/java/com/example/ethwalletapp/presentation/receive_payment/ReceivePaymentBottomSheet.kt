package com.example.ethwalletapp.presentation.receive_payment

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethwalletapp.shared.components.SecondaryButton
import com.example.ethwalletapp.shared.theme.Gray24
import com.example.ethwalletapp.shared.theme.Gray9
import com.example.ethwalletapp.shared.theme.Primary5

@Composable
fun ReceivePaymentBottomSheet(
  selectedAccountAddress: String?
) {
  val clipboardManager = LocalClipboardManager.current

  Scaffold(backgroundColor = Gray24) {
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
        onClick = {
          clipboardManager.setText(AnnotatedString(selectedAccountAddress ?: ""))
        },
        text = selectedAccountAddress ?: "?",
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
}