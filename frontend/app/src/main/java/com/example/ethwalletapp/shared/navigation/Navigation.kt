package com.example.ethwalletapp.shared.navigation

import CreateAccountScreen
import StartScreen
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ethwalletapp.ui.create_account.CreateAccountViewModel

@Composable
fun Navigation() {
  val navController = rememberNavController()

  NavHost(
    navController = navController,
    startDestination = Screen.StartScreen.route
  ) {
    composable(route = Screen.StartScreen.route) { StartScreen(navController) }
    composable(route = Screen.CreateAccountScreen.route) {
      val viewModel: CreateAccountViewModel = hiltViewModel()
      CreateAccountScreen(navController, viewModel)
    }
  }
}