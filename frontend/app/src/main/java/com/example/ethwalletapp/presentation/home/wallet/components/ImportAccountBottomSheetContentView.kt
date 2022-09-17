package com.example.ethwalletapp.presentation.home.wallet.components

import PrimaryButton
import TextInput
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethwalletapp.shared.components.ErrorBanner

@Composable
fun ImportAccountBottomSheetContentView(
  onBack: () -> Unit,
  importAccountPrivateKey: String,
  setImportAccountPrivateKey: (value: String) -> Unit,
  isImportAccountPrivateKeyValid: Boolean,
  onImport: () -> Unit,
  onImportHasError: Boolean
) {
  Column {
    if (onImportHasError) {
      ErrorBanner(
        title = "Error! Retry it again",
        description = "Error importing existing account"
      )
    }
    Spacer(Modifier.height(6.dp))
    Box(
      modifier = Modifier
        .padding(horizontal = 10.dp)
        .fillMaxWidth()
    ) {
      IconButton(
        onClick = onBack,
        modifier = Modifier.align(Alignment.CenterStart)
      ) {
        Icon(
          Icons.Outlined.KeyboardArrowLeft,
          contentDescription = "Back to account",
          tint = Color.White
        )
      }
      Text(
        text = "Import Account",
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White,
        modifier = Modifier.align(Alignment.Center)
      )
    }
    Spacer(Modifier.height(40.dp))
    Column(Modifier.padding(horizontal = 24.dp)) {
      Text(
        text = "Imported accounts are viewable in your wallet but are not recoverable with your DeGe secret recovery phrase.",
        fontSize = 14.sp,
        color = Color.White
      )
      Spacer(Modifier.height(28.dp))
      Text(
        text = "Paste your private key string",
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White,
      )
      Spacer(Modifier.height(20.dp))
      TextInput(
        value = importAccountPrivateKey,
        onValueChange = setImportAccountPrivateKey,
        label = "Private Key",
        helperText = if (!isImportAccountPrivateKeyValid) "Invalid private key" else null,
        hasError = !isImportAccountPrivateKeyValid
      )
      Spacer(Modifier.weight(1f).height(80.dp))
      PrimaryButton(
        onClick = onImport,
        text = "Import"
      )
      Spacer(Modifier.height(42.dp))
    }
  }
}

@Preview
@Composable
private fun ImportAccountBottomSheetContentViewPreview() {
  ImportAccountBottomSheetContentView(
    onBack = {},
    importAccountPrivateKey = "",
    onImport = {},
    isImportAccountPrivateKeyValid = true,
    setImportAccountPrivateKey = {},
    onImportHasError = false
  )
}