package com.example.ethwalletapp.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ethwalletapp.shared.navigation.Navigator
import com.example.ethwalletapp.shared.navigation.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val b: Bundle? = intent.extras
    val hasAccount: Boolean = b?.getBoolean("hasAccount") ?: false
    setContent {
      Navigator(
        startDestination = if (hasAccount) Screen.HomeScreen.route else Screen.StartScreen.route
      )
    }
  }
}

