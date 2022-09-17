package com.example.ethwalletapp.presentation.send_payment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ethwalletapp.presentation.send_payment.components.SendToView
import com.example.ethwalletapp.shared.theme.Gray24
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SendPaymentScreen(
  viewModel: ISendPaymentScreenViewModel,
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
            toOwnAccount = uiState.toOwnAccount,
            setToOwnAccount = viewModel::setToOwnAccount,
            isToOwnAccountSelected = viewModel::isToOwnAccountSelected,
            onNext = { scope.launch { pagerState.animateScrollToPage(1) } }
          )
          1 -> {}
          2 -> {}
        }
      }
    }
  }
}

@Preview
@Composable
private fun SendPaymentScreenPreview() {
  SendPaymentScreen(SendPaymentScreenViewModelMock(), "")
}