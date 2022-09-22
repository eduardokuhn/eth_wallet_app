package com.example.ethwalletapp.presentation.token_overview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Send
import androidx.compose.runtime.Composable
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
import com.example.ethwalletapp.presentation.token_overview.components.TransactionListItem
import com.example.ethwalletapp.shared.components.SecondaryButton
import com.example.ethwalletapp.shared.theme.Gradient07
import com.example.ethwalletapp.shared.theme.Gray24
import com.example.ethwalletapp.shared.theme.Primary5
import com.example.ethwalletapp.shared.utils.ViewState
import com.example.ethwalletapp.shared.utils.weiToEther

@Composable
fun TokenOverviewScreen(
  navController: NavController?,
  viewModel: ITokenOverviewViewModel,
  selectedAccountAddress: String?
) {
  val uiState = viewModel.uiState.value

  Scaffold(
    backgroundColor = Gray24
  ) { padding ->
    Column(
      modifier = Modifier
        .padding(padding)
        .fillMaxSize()
    ) {
      if (uiState.viewState != ViewState.Loading) {
        Spacer(Modifier.height(44.dp))
        Box(
          Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
        ) {
          IconButton(
            onClick = {},
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
              onClick = { },
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
              onClick = {},
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
          Spacer(Modifier.height(12.dp))
          LazyColumn(Modifier.weight(1f)) {
            items(uiState.transactions) { transaction ->
              TransactionListItem(
                isSentTransaction = true,
                transaction = transaction,
                valueInUsd = uiState.ethUsdPrice.toString()
              )
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

