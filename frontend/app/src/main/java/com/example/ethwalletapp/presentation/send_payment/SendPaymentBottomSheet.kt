package com.example.ethwalletapp.presentation.send_payment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.ethwalletapp.presentation.send_payment.components.AmountView
import com.example.ethwalletapp.presentation.send_payment.components.ConfirmView
import com.example.ethwalletapp.presentation.send_payment.components.SendToView
import com.example.ethwalletapp.shared.theme.Gray24
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SendPaymentBottomSheet(
  navController: NavController?,
  viewModel: ISendPaymentBottomSheetViewModel,
  fromAccountAddress: String?
) {
  Scaffold(
    backgroundColor = Gray24
  ) { padding ->
    val uiState = viewModel.uiState.value
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    Column(
      modifier = Modifier
        .padding(padding)
        .fillMaxSize()
    ) {
      HorizontalPager(
        count = 3,
        state = pagerState,
        modifier = Modifier.weight(1f)
      ) { page ->
        when (page) {
          0 -> SendToView(
            accounts = uiState.accounts,
            fromAccount = uiState.fromAccount,
            setFromAccount = viewModel::setFromAccount,
            isFromAccountSelected = viewModel::isFromAccountSelected,
            balances = uiState.balances,
            fromAccountBalance = uiState.fromAccountBalance,
            toAccountInput = uiState.toAccountInput,
            setToAccountInput = viewModel::setToAccountInput,
            toAccountInputHelperText = uiState.toAccountInputHelperText,
            isToAccountInputValid = uiState.isToAccountInputValid,
            toAccount = uiState.toAccount,
            setToAccount = viewModel::setToAccount,
            isToAccountSelected = viewModel::isToAccountSelected,
            validateToAccountInput = viewModel::validateToAccountInput,
            onNext = { scope.launch { pagerState.animateScrollToPage(1) } },
            onClose = { navController?.popBackStack() },
          )
          1 -> AmountView(
            hasSufficientFunds = uiState.hasSufficientFunds,
            valueInputInEther = uiState.valueInputInEther,
            valueInputInUsd = uiState.valueInputInUsd,
            valueInput = uiState.valueInput,
            setValueInput = viewModel::setValueInput,
            isValueInputInUsd = uiState.isValueInputInUsd,
            toggleIsValueInputInUsd = viewModel::toggleIsValueInputInUsd,
            fromAccountBalance = uiState.fromAccountBalance,
            useMax = viewModel::useMax,
            onBack = { scope.launch { pagerState.animateScrollToPage(0) } },
            onClose = { navController?.popBackStack() },
            onNext = { scope.launch { pagerState.animateScrollToPage(2) } }
          )
          2 -> ConfirmView(
            confirmViewState = uiState.confirmViewState,
            valueInputInEther = uiState.valueInputInEther,
            fromAccount = uiState.fromAccount,
            fromAccountBalance = uiState.fromAccountBalance,
            toAccount = uiState.toAccount,
            networkFeeInEther = uiState.networkFeeInEther,
            totalValueInEther = uiState.totalValueInEther,
            totalValueInUsd = uiState.totalValueInUsd,
            onBack = { scope.launch { pagerState.animateScrollToPage(1) } },
            onClose = { navController?.popBackStack() },
            onSend = {
              scope.launch {
                val ok = viewModel.sendTransaction()
                if (ok) navController?.popBackStack()
              }
            }
          )
        }
      }
    }
  }
}

@Preview
@Composable
private fun SendPaymentScreenPreview() {
  SendPaymentBottomSheet(null, SendPaymentBottomSheetViewModelMock(), "")
}