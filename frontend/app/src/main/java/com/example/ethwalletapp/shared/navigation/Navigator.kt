package com.example.ethwalletapp.shared.navigation

import StartScreen
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ethwalletapp.presentation.create_wallet.CreateWalletScreen
import com.example.ethwalletapp.presentation.create_wallet.CreateWalletViewModel
import com.example.ethwalletapp.presentation.import_wallet.ImportWalletScreen
import com.example.ethwalletapp.presentation.import_wallet.ImportWalletViewModel

@Composable
fun Navigator() {
  val navController = rememberNavController()

  NavHost(
    navController = navController,
    startDestination = Screen.StartScreen.route
  ) {
    composable(route = Screen.StartScreen.route) { StartScreen(navController) }
    composable(route = Screen.CreateWalletScreen.route) {
      val viewModel: CreateWalletViewModel = hiltViewModel()
      CreateWalletScreen(navController, viewModel)
    }
    composable(route = Screen.ImportWalletScreen.route) {
      val viewModel: ImportWalletViewModel = hiltViewModel()
      ImportWalletScreen(navController, viewModel)
    }
    composable(route = Screen.HomeScreen.route) {

    }
  }
}