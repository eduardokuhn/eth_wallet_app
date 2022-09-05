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
import com.example.ethwalletapp.presentation.create_wallet.components.ConfirmSecretRecoveryPhraseStep
import com.example.ethwalletapp.presentation.create_wallet.components.CreateWalletStep
import com.example.ethwalletapp.presentation.create_wallet.components.SecureWalletStep
import com.example.ethwalletapp.presentation.create_wallet.components.StepProgressBar
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
  val uiState = viewModel.uiState

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
    ) {
      Spacer(modifier = Modifier.height(44.dp))
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = {
            println(pagerState.currentPage)
            when (pagerState.currentPage) {
              0 -> navController?.navigate(Screen.StartScreen.route)
              1 -> {
                val step = CreateAccountStep.CreateWallet
                viewModel.setCurrentStep(step)
                scope.launch { pagerState.animateScrollToPage(step.index()) }
              }
              else -> {
                val step = CreateAccountStep.SecureWallet
                viewModel.setCurrentStep(step)
                scope.launch { pagerState.animateScrollToPage(step.index()) }
              }
            }
          },
        ) {
          Icon(
            Icons.Filled.ArrowBack,
            contentDescription = "Back to start screen",
            tint = Color.White
          )
        }
        StepProgressBar(
          currentStep = uiState.value.currentStep,
          modifier = Modifier.fillMaxWidth()
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
          0 -> CreateWalletStep(
            createWallet = {
              val success = viewModel.createWallet()
              if (success) {
                val step = CreateAccountStep.SecureWallet
                viewModel.setCurrentStep(step)
                scope.launch { pagerState.animateScrollToPage(step.index()) }
              }
            },
            uiState = viewModel.uiState.value,
            setPassword = viewModel::setPassword,
            toggleShowPassword = viewModel::toggleShowPassword,
            setPasswordConfirmation = viewModel::setPasswordConfirmation,
            toggleShowPasswordConfirmation = viewModel::toggleShowPasswordConfirmation,
            toggleIsChecked = viewModel::toggleIsChecked
          )
          1 -> SecureWalletStep()
          2 -> ConfirmSecretRecoveryPhraseStep()
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