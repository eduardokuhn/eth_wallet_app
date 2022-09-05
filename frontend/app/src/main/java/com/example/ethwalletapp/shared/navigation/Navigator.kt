package com.example.ethwalletapp.shared.navigation

import com.example.ethwalletapp.presentation.create_wallet.CreateWalletScreen
import StartScreen
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ethwalletapp.presentation.create_wallet.CreateWalletViewModel

@Composable
fun Navigator() {
  val navController = rememberNavController()

  NavHost(
    navController = navController,
    startDestination = Screen.StartScreen.route
  ) {
    composable(route = Screen.StartScreen.route) { StartScreen(navController) }
    composable(route = Screen.CreateAccountScreen.route) {
      val viewModel: CreateWalletViewModel = hiltViewModel()
      CreateWalletScreen(navController, viewModel)
    }
  }
}