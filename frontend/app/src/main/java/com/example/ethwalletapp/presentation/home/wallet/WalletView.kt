package com.example.ethwalletapp.presentation.home.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.ChangeCircle
import androidx.compose.material.icons.outlined.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethwalletapp.R
import com.example.ethwalletapp.presentation.home.wallet.components.AccountBottomSheetContent
import com.example.ethwalletapp.presentation.home.wallet.components.TokenListItem
import com.example.ethwalletapp.shared.components.ErrorBanner
import com.example.ethwalletapp.shared.components.SecondaryButton
import com.example.ethwalletapp.shared.theme.Gradient07
import com.example.ethwalletapp.shared.theme.Gray24
import com.example.ethwalletapp.shared.theme.Green5
import com.example.ethwalletapp.shared.theme.Primary5
import com.example.ethwalletapp.shared.utils.EthereumUnitConverter
import com.example.ethwalletapp.shared.utils.ViewState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WalletView(
  viewModel: IWalletViewViewModel,
  setShowTabBar: (value: Boolean) -> Unit
) {
  val uiState = viewModel.uiState.value
  val sheetState = rememberModalBottomSheetState(
    initialValue = ModalBottomSheetValue.Hidden,
    confirmStateChange = {
      setShowTabBar(it == ModalBottomSheetValue.Hidden)
      it != ModalBottomSheetValue.HalfExpanded
    }
  )
  val scope = rememberCoroutineScope()

  ModalBottomSheetLayout(
    sheetState = sheetState,
    sheetContent = {
      AccountBottomSheetContent(
        accounts = uiState.accounts,
        setSelectedAccount = viewModel::setSelectedAccount,
        balances = uiState.balances,
        newAccountName = uiState.newAccountName,
        setNewAccountName = viewModel::setNewAccountName,
        createNewAccount = {
          scope.launch {
            val ok = viewModel.createNewAccount()
            if (ok) sheetState.hide()
          }
        },
        createNewAccountHasError = uiState.createNewAccountHasError,
        importAccount = { /*TODO*/ },
      )
    },
    sheetBackgroundColor = Gray24,
    modifier = Modifier.fillMaxSize()
  ) {
    Column(
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.fillMaxSize()
    ) {
      if (uiState.viewState != ViewState.Loading) {
        if (uiState.viewState == ViewState.Error) {
          ErrorBanner(
            title = "Error! Retry it again",
            description = "There was an error loading data"
          )
        }

        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxSize()
        ) {
          Spacer(Modifier.height(44.dp))
          Box(Modifier.fillMaxWidth()) {
            Box(
              modifier = Modifier
                .clickable {
                  scope.launch {
                    if (sheetState.isVisible) {
                      sheetState.hide()
                      setShowTabBar(true)
                    } else {
                      setShowTabBar(false)
                      sheetState.show()
                    }
                  }
                }
                .size(36.dp)
                .align(Alignment.CenterStart)
                .background(Green5, CircleShape)
            ) {
              Text(
                text =
                  if (uiState.selectedAccount != null) (uiState.selectedAccount.addressIndex + 1).toString()
                  else "?",
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
              )
              Box(
                modifier = Modifier
                  .size(18.dp)
                  .offset(x = 25.dp, y = 18.dp)
                  .clip(CircleShape)
                  .background(Primary5)
              ) {
                Icon(
                  Icons.Outlined.ChangeCircle,
                  contentDescription = "Switch account",
                  tint = Color.White
                )
              }
            }
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                .clickable { }
                .align(Alignment.Center)
            ) {
              Text(
                text = uiState.network.toString(),
                fontSize = 13.sp,
                color = Color.White
              )
              Spacer(Modifier.width(4.dp))
              Icon(
                Icons.Outlined.ArrowDropDown,
                contentDescription = "Change network",
                tint = Color.White
              )
            }
          }
          Spacer(Modifier.height(48.dp))
          Text(
            text =
            if (uiState.selectedAccountBalance != null)
              "${EthereumUnitConverter.weiToEther(uiState.selectedAccountBalance.balance)} ETH"
            else "__.__ ETH",
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
              onClick = { /*TODO*/ },
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
              onClick = { /*TODO*/ },
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
          Text(
            text = "Token",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier.align(Alignment.Start)
          )
          Spacer(Modifier.height(28.dp))
          TokenListItem(
            icon = painterResource(R.drawable.ic_ethereum),
            name = "Ethereum",
            abbr = "ETH",
            value = uiState.ethUsdPrice,
            balance = uiState.selectedAccountBalance?.balance
          )
        }
      } else CircularProgressIndicator(color = Primary5)
    }
  }
}

@Preview
@Composable
private fun WalletViewPreview() {
  fun setShowTabBar(value: Boolean) {}
  WalletView(WalletViewViewModelMock(), ::setShowTabBar)
}