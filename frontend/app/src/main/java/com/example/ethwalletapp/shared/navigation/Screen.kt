package com.example.ethwalletapp.shared.navigation

sealed class Screen(val route: String) {
  object StartScreen: Screen("start_screen")
  object CreateAccountScreen: Screen("create_account_screen")

  fun withArgs(vararg args: String): String {
    return buildString {
      append(route)
      args.forEach { arg ->
        append("/$arg")
      }
    }
  }
}
