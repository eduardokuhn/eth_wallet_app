package com.example.ethwalletapp.shared.navigation

import StartScreen
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ethwalletapp.presentation.create_wallet.CreateWalletScreen
import com.example.ethwalletapp.presentation.create_wallet.CreateWalletViewModel
import com.example.ethwalletapp.presentation.home.HomeScreen
import com.example.ethwalletapp.presentation.import_wallet.ImportWalletScreen
import com.example.ethwalletapp.presentation.import_wallet.ImportWalletViewModel
import com.example.ethwalletapp.presentation.send_payment.SendPaymentScreen
import com.example.ethwalletapp.presentation.send_payment.SendPaymentScreenViewModel

@Composable
fun Navigator(startDestination: String) {
  val navController = rememberNavController()

  NavHost(
    navController = navController,
    startDestination = startDestination
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
      HomeScreen()
    }
    composable(
      route = "${Screen.SendPaymentScreen.route}/{fromAccountAddress}",
      arguments = listOf(navArgument("fromAccountAddress") { type = NavType.StringType })
    ) { backStackEntry ->
      val viewModel: SendPaymentScreenViewModel = hiltViewModel()
      SendPaymentScreen(viewModel, backStackEntry.arguments?.getString("fromAccountAddress"))
    }
  }
}