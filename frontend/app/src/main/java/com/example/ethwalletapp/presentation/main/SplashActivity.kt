package com.example.ethwalletapp.presentation.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.example.ethwalletapp.data.services.IAccountService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.properties.Delegates

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity(): ComponentActivity() {
  @Inject
  lateinit var accountService: IAccountService

  override fun onCreate(savedInstanceState: Bundle?) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
      val splashScreen = installSplashScreen()
      splashScreen.setKeepOnScreenCondition { true }
    }

    var hasAccount by Delegates.notNull<Boolean>()

    super.onCreate(savedInstanceState)
    lifecycleScope.launchWhenCreated {
      hasAccount = accountService.hasAccount()

      delay(3000)
      val intent = Intent(this@SplashActivity, MainActivity::class.java)
      intent.putExtra("hasAccount", hasAccount)
      startActivity(intent)
      finish()
    }
  }
}