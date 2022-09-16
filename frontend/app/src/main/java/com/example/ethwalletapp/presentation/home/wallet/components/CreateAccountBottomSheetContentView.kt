package com.example.ethwalletapp.presentation.home.wallet.components

import PrimaryButton
import TextInput
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
fun CreateAccountBottomSheetContentView(
  onBack: () -> Unit,
  newAccountName: String,
  setNewAccountName: (value: String) -> Unit,
  onCreate: () -> Unit,
  onCreateHasError: Boolean
) {
  Column {
    if (onCreateHasError) {
      ErrorBanner(
        title = "Error! Retry it again",
        description = "Error creating new account"
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
          Icons.Filled.ArrowBack,
          contentDescription = "Back to account",
          tint = Color.White
        )
      }
      Text(
        text = "Create New Account",
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White,
        modifier = Modifier.align(Alignment.Center)
      )
    }
    Spacer(Modifier.height(40.dp))
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.padding(horizontal = 24.dp)
    ) {
      TextInput(
        value = newAccountName,
        onValueChange = setNewAccountName,
        label = "Account Name"
      )
      Spacer(Modifier.weight(1f).height(80.dp))
      PrimaryButton(
        onClick = onCreate,
        text = "Create"
      )
      Spacer(Modifier.height(42.dp))
    }
  }
}

@Preview
@Composable
private fun CreateAccountBottomSheetContentViewPreview() {
  CreateAccountBottomSheetContentView(
    onBack = {},
    onCreate = {},
    newAccountName = "",
    setNewAccountName = {},
    onCreateHasError = false
  )
}