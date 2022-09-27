package com.example.ethwalletapp.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ethwalletapp.shared.navigation.Screen
import com.example.ethwalletapp.shared.theme.Gray24
import kotlinx.coroutines.launch

@Composable
fun SettingsView(
  navController: NavController?,
  viewModel: SettingsViewViewModel
) {
  val scope = rememberCoroutineScope()

  Scaffold(
    backgroundColor = Gray24
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .padding(horizontal = 24.dp)
    ) {
      Spacer(Modifier.height(44.dp))
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 10.dp)
      ) {
        Text(
          text = "Settings",
          fontSize = 18.sp,
          fontWeight = FontWeight.SemiBold,
          color = Color.White,
          modifier = Modifier.align(Alignment.Center)
        )
      }
      Spacer(Modifier.height(24.dp))
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clickable {
            scope.launch {
              viewModel.resetWallet()
              navController?.backQueue?.clear()
              navController?.navigate(Screen.StartScreen.route)
            }
          }
          .fillMaxWidth()
      ) {
        Icon(
          Icons.Outlined.Logout,
          contentDescription = "Reset wallet",
          tint = Color.White
        )
        Spacer(Modifier.width(4.dp))
        Text(
          text = "Reset wallet",
          fontSize = 18.sp,
          color = Color.White,
        )
      }
    }
  }
}