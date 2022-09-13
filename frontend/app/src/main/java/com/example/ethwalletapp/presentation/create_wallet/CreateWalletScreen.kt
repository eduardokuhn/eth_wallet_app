package com.example.ethwalletapp.presentation.create_wallet

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ethwalletapp.presentation.create_wallet.components.*
import com.example.ethwalletapp.shared.navigation.Screen
import com.example.ethwalletapp.shared.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CreateWalletScreen(
  navController: NavController?,
  viewModel: ICreateAccountViewModel,
) {
  val uiState by viewModel.uiState

  val pagerState = rememberPagerState()
  val scope = rememberCoroutineScope()

  Scaffold(
    backgroundColor = Gray24
  ) { padding ->
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier
        .padding(padding)
        .fillMaxSize()
        .padding(horizontal = 24.dp)
    ) {
      Spacer(modifier = Modifier.height(44.dp))
      Box(Modifier.fillMaxWidth()) {
        IconButton(
          onClick = {
            when (pagerState.currentPage) {
              0 -> navController?.navigate(Screen.StartScreen.route)
              1 -> {
                val step = CreateWalletStep.CreatePassword
                viewModel.setCurrentStep(step)
                scope.launch { pagerState.animateScrollToPage(step.index()) }
              }
              else -> {
                val step = CreateWalletStep.SecureWallet
                viewModel.setCurrentStep(step)
                scope.launch { pagerState.animateScrollToPage(step.index()) }
              }
            }
          },
          modifier = Modifier.align(Alignment.CenterStart)
        ) {
          Icon(
            Icons.Filled.ArrowBack,
            contentDescription = "Back to start screen",
            tint = Color.White
          )
        }
        StepProgressBar(
          currentStep = uiState.currentStep,
          modifier = Modifier.align(Alignment.Center)
        )
      }
      HorizontalPager(
        count = 3,
        state = pagerState,
        userScrollEnabled = false,
        modifier = Modifier
          .fillMaxSize()
      ) { index ->
        when (index) {
          0 -> CreatePasswordStep(
            createPassword = {
              val success = viewModel.createPassword()
              if (success) {
                val step = CreateWalletStep.SecureWallet
                viewModel.setCurrentStep(step)
                scope.launch { pagerState.animateScrollToPage(step.index()) }
              }
            },
            uiState = uiState,
            setPassword = viewModel::setPassword,
            toggleShowPassword = viewModel::toggleShowPassword,
            setPasswordConfirmation = viewModel::setPasswordConfirmation,
            toggleShowPasswordConfirmation = viewModel::toggleShowPasswordConfirmation,
            toggleIsChecked = viewModel::toggleIsChecked
          )
          1 -> SecureWalletStep(
            next = {
              val step = CreateWalletStep.ConfirmSecretRecoveryPhrase
              viewModel.setCurrentStep(step)
              scope.launch { pagerState.animateScrollToPage(step.index()) }
             },
            uiState = uiState,
            toggleShowSecretRecoveryPhrase = viewModel::toggleShowSecretRecoveryPhrase
          )
          2 -> ConfirmSecretRecoveryPhraseStep(
            next = {
              scope.launch {
                val success = viewModel.createWallet()
                if (success)
                  navController?.navigate(Screen.HomeScreen.route)
              }
            },
            uiState = uiState,
            setSecretRecoveryPhraseConfirmation = viewModel::setSecretRecoveryPhraseConfirmation
          )
        }
      }
    }
  }
}

@Preview
@Composable
private fun CreateWalletScreenPreview() {
  val viewModelMock = CreateWalletViewModelMock()
  CreateWalletScreen(null, viewModelMock)
}