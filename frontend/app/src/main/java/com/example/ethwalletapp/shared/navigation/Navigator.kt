package com.example.ethwalletapp.shared.navigation

import StartScreen
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
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
import com.example.ethwalletapp.presentation.send_payment.SendPaymentBottomSheet
import com.example.ethwalletapp.presentation.send_payment.SendPaymentBottomSheetViewModel
import com.example.ethwalletapp.presentation.token_overview.TokenOverviewScreen
import com.example.ethwalletapp.presentation.token_overview.TokenOverviewViewModel
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterialApi::class)
@Composable
fun Navigator(startDestination: String) {
  val bottomSheetNavigator = rememberBottomSheetNavigator()
  val navController = rememberNavController(bottomSheetNavigator)

  ModalBottomSheetLayout(bottomSheetNavigator) {
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
        HomeScreen(navController)
      }
      bottomSheet(
        route = "${Screen.SendPaymentBottomSheet.route}/{fromAccountAddress}",
        arguments = listOf(navArgument("fromAccountAddress") { type = NavType.StringType } )
      ) { backStackEntry ->
        val viewModel: SendPaymentBottomSheetViewModel = hiltViewModel()
        SendPaymentBottomSheet(navController, viewModel, backStackEntry.arguments?.getString("fromAccountAddress"))
      }
      composable(
        route = "${Screen.TokenOverview.route}/{selectedAccountAddress}",
        arguments = listOf(navArgument("selectedAccountAddress") { type = NavType.StringType} )
      ) { backStackEntry ->
        val viewModel: TokenOverviewViewModel = hiltViewModel()
        TokenOverviewScreen(navController, viewModel, backStackEntry.arguments?.getString("selectedAccountAddress"))
      }
    }
  }
}