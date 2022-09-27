package com.example.ethwalletapp.presentation.token_overview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.ethwalletapp.presentation.token_overview.components.TransactionListItem
import com.example.ethwalletapp.shared.components.ErrorBanner
import com.example.ethwalletapp.shared.components.SecondaryButton
import com.example.ethwalletapp.shared.navigation.Screen
import com.example.ethwalletapp.shared.theme.Gradient07
import com.example.ethwalletapp.shared.theme.Gray24
import com.example.ethwalletapp.shared.theme.Primary5
import com.example.ethwalletapp.shared.utils.ViewState
import com.example.ethwalletapp.shared.utils.weiToEther
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun TokenOverviewScreen(
  navController: NavController?,
  viewModel: ITokenOverviewViewModel,
  selectedAccountAddress: String?
) {
  val uiState = viewModel.uiState.value
  val scope = rememberCoroutineScope()
  val lazyTransactions = uiState.transactions.collectAsLazyPagingItems()

  Scaffold(
    backgroundColor = Gray24
  ) { padding ->
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier
        .padding(padding)
        .fillMaxSize()
    ) {
      if (uiState.viewState != ViewState.Loading) {
        lazyTransactions.apply {
          if (loadState.refresh is LoadState.Error) {
            ErrorBanner(
              title = "Error! Retry it again",
              description = "Error getting transactions"
            )
          }
        }

        Spacer(Modifier.height(44.dp))
        Box(
          Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
        ) {
          IconButton(
            onClick = { navController?.popBackStack() },
            modifier = Modifier.align(Alignment.CenterStart)
          ) {
            Icon(
              Icons.Outlined.KeyboardArrowLeft,
              contentDescription = "Back to wallet",
              tint = Color.White
            )
          }
          Text(
            text = "ETH",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
          )
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clickable { scope.launch { viewModel.refreshData() } }
              .align(Alignment.CenterEnd)
          ) {
            Icon(
              Icons.Outlined.Sync,
              contentDescription = "Synchronize data",
              tint = Color.White
            )
            Spacer(Modifier.width(4.dp))
            Text(
              text = "Sync",
              fontSize = 14.sp,
              color = Color.White
            )
            Spacer(Modifier.width(20.dp))
          }
        }
        Spacer(Modifier.height(48.dp))
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxSize()
        ) {
          Text(
            text = "${uiState.selectedAccountBalance?.balance?.weiToEther() ?: "__.__"} ETH",
            fontSize = 40.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
              .graphicsLayer(alpha = 0.99f)
              .drawWithCache {
                val brush = Brush.horizontalGradient(Gradient07)
                onDrawWithContent {
                  drawContent()
                  drawRect(brush, blendMode = BlendMode.SrcAtop)
                }
              }
          )
          Spacer(Modifier.height(20.dp))
          Text(
            text = "$${uiState.ethUsdPrice ?: "__,__"}",
            fontSize = 15.sp,
            color = Color.White
          )
          Spacer(Modifier.height(28.dp))
          Row {
            SecondaryButton(
              onClick = { navController?.navigate("${Screen.SendPaymentBottomSheet.route}/$selectedAccountAddress") },
              text = "Send",
              leadingIcon = {
                Icon(
                  Icons.Outlined.Send,
                  contentDescription = "Send Ethereum",
                  tint = Color.White
                )
              },
              modifier = Modifier.height(44.dp)
            )
            Spacer(Modifier.width(10.dp))
            SecondaryButton(
              onClick = { navController?.navigate("${Screen.ReceivePaymentBottomSheet.route}/$selectedAccountAddress") },
              text = "Receive",
              leadingIcon = {
                Icon(
                  Icons.Outlined.AccountBalanceWallet,
                  contentDescription = "Receive Ethereum",
                  tint = Color.White
                )
              },
              modifier = Modifier.height(44.dp)
            )
          }
          Spacer(Modifier.height(28.dp))
          LazyColumn(Modifier.weight(1f)) {
            items(lazyTransactions) { transaction ->
              if (transaction != null) {
                TransactionListItem(
                  isSentTransaction = transaction.transaction.from == uiState.selectedAccount?.address,
                  transaction = transaction,
                  valueInUsd = transaction.transaction.value?.weiToEther()?.times(BigDecimal(uiState.ethUsdPrice ?: 0.0))?.setScale(2, RoundingMode.HALF_EVEN).toString()
                )
                Spacer(Modifier.height(28.dp))
              }
            }

            lazyTransactions.apply {
              if (loadState.refresh is LoadState.Loading) {
                item { CircularProgressIndicator(color = Primary5) }
              }
            }
          }
        }
      } else CircularProgressIndicator(color = Primary5)
    }
  }
}

@Preview
@Composable
private fun TokenOverviewScreenPreview() {
  val viewModel = TokenOverviewViewModelMock()
  TokenOverviewScreen(null, viewModel, "")
}

