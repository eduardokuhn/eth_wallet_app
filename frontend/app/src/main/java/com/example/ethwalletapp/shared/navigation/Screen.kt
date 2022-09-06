package com.example.ethwalletapp.shared.navigation

sealed class Screen(val route: String) {
  object StartScreen: Screen("start_screen")
  object CreateWalletScreen: Screen("create_wallet_screen")
  object ImportWalletScreen: Screen("import_wallet_screen")
  object HomeScreen: Screen("home_screen")

  fun withArgs(vararg args: String): String {
    return buildString {
      append(route)
      args.forEach { arg ->
        append("/$arg")
      }
    }
  }
}
