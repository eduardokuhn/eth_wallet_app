package com.example.ethwalletapp.presentation.home.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ethwalletapp.R
import com.example.ethwalletapp.presentation.home.wallet.components.AccountBottomSheetContent
import com.example.ethwalletapp.presentation.home.wallet.components.NetworkBottomSheetContent
import com.example.ethwalletapp.presentation.home.wallet.components.ReceivePaymentBottomSheetContent
import com.example.ethwalletapp.presentation.home.wallet.components.TokenListItem
import com.example.ethwalletapp.shared.components.ErrorBanner
import com.example.ethwalletapp.shared.components.SecondaryButton
import com.example.ethwalletapp.shared.components.SubmittedBanner
import com.example.ethwalletapp.shared.navigation.Screen
import com.example.ethwalletapp.shared.theme.Gradient07
import com.example.ethwalletapp.shared.theme.Gray24
import com.example.ethwalletapp.shared.theme.Green5
import com.example.ethwalletapp.shared.theme.Primary5
import com.example.ethwalletapp.shared.utils.ViewState
import com.example.ethwalletapp.shared.utils.weiToEther
import kotlinx.coroutines.launch

sealed class WalletViewBottomSheetContent() {
  object AccountBottomSheet: WalletViewBottomSheetContent()
  object NetworkBottomSheet: WalletViewBottomSheetContent()
  object ReceivePaymentBottomSheet: WalletViewBottomSheetContent()
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WalletView(
  navController: NavController?,
  viewModel: IWalletViewViewModel,
  setShowTabBar: (value: Boolean) -> Unit
) {
  val uiState = viewModel.uiState.value

  var currentBottomSheetContent: WalletViewBottomSheetContent? by remember {
    mutableStateOf(null)
  }
  val sheetState = rememberModalBottomSheetState(
    initialValue = ModalBottomSheetValue.Hidden,
    confirmStateChange = {
      setShowTabBar(it == ModalBottomSheetValue.Hidden)
      if (it == ModalBottomSheetValue.Hidden) currentBottomSheetContent = null
      it != ModalBottomSheetValue.HalfExpanded
    }
  )

  val scope = rememberCoroutineScope()

  val clipboardManager = LocalClipboardManager.current

  val snackbarHostState = remember { SnackbarHostState() }

  fun openSheet(content: WalletViewBottomSheetContent) {
    currentBottomSheetContent = content
    setShowTabBar(false)
    scope.launch { sheetState.show() }
  }

  fun closeSheet() {
    scope.launch { sheetState.hide() }
    currentBottomSheetContent = null
    setShowTabBar(true)
  }

  BoxWithConstraints(Modifier.fillMaxSize()) {
    ModalBottomSheetLayout(
      sheetState = sheetState,
      sheetContent = {
        // To prevent crash
        Spacer(modifier = Modifier.height(1.dp))
        currentBottomSheetContent?.let { currentSheet ->
          when (currentSheet) {
            WalletViewBottomSheetContent.AccountBottomSheet -> AccountBottomSheetContent(
              accounts = uiState.accounts,
              balances = uiState.balances,
              selectAccount = { account, balance ->
                viewModel.selectAccount(account, balance)
                closeSheet()
              },
              isAccountSelected = viewModel::isAccountSelected,
              newAccountName = uiState.newAccountName,
              setNewAccountName = viewModel::setNewAccountName,
              createNewAccount = {
                var ok = false
                scope.launch {
                  ok = viewModel.createNewAccount()
                  if (ok) closeSheet()
                }
                ok
              },
              createNewAccountHasError = uiState.createNewAccountHasError,
              importAccountPrivateKey = uiState.importAccountPrivateKey,
              setImportAccountPrivateKey = viewModel::setImportAccountPrivateKey,
              isImportAccountPrivateKeyValid = uiState.isImportAccountPrivateKeyValid,
              importAccount = {
                var ok = false
                scope.launch {
                  ok = viewModel.importAccount()
                  if (ok) closeSheet()
                }
                ok
              },
              importAccountHasError = uiState.importAccountHasError
            )
            WalletViewBottomSheetContent.NetworkBottomSheet -> NetworkBottomSheetContent(
              selectNetwork = viewModel::setSelectedNetwork,
              isNetworkSelected = viewModel::isNetworkSelected,
              onClose = { closeSheet() }
            )
            WalletViewBottomSheetContent.ReceivePaymentBottomSheet -> ReceivePaymentBottomSheetContent(
              address = uiState.selectedAccount?.address,
              copyToClipboard = {
                clipboardManager.setText(AnnotatedString(uiState.selectedAccount?.address?.hex ?: ""))
              }
            )
          }
        }
      },
      scrimColor = Color.Black.copy(alpha = 0.65f),
      sheetBackgroundColor = Gray24
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
                    if (sheetState.isVisible) closeSheet()
                    else openSheet(WalletViewBottomSheetContent.AccountBottomSheet)
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
                  .clickable {
                    if (sheetState.isVisible) closeSheet()
                    else openSheet(WalletViewBottomSheetContent.NetworkBottomSheet)
                  }
                  .align(Alignment.Center)
              ) {
                Text(
                  text = uiState.selectedNetwork.toString(),
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
              }
            }
            Spacer(Modifier.height(48.dp))
            Text(
              text =
                if (uiState.selectedAccountBalance != null)
                  "${uiState.selectedAccountBalance.balance.weiToEther()} ETH"
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
                onClick = { navController?.navigate("${Screen.SendPaymentBottomSheet.route}/${uiState.selectedAccount?.address?.hex}") },
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
                onClick = {
                  if (sheetState.isVisible) closeSheet()
                  else openSheet(WalletViewBottomSheetContent.ReceivePaymentBottomSheet)
                },
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
              tokenPrice = uiState.ethUsdPrice,
              balance = uiState.selectedAccountBalance?.balance,
              onClick = { navController?.navigate("${Screen.TokenOverview.route}/${uiState.selectedAccount?.address?.hex}") },
            )
          }
        } else CircularProgressIndicator(color = Primary5)
      }
      SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { snackData ->
          when (snackData.message) {
            "Pending" -> SubmittedBanner(
              title = "Transaction Submitted",
              description = "Waiting for confirmation"
            )
            "Success" -> {}
            "Failed" -> {}
          }
        }
      )
    }
  }
}

@Preview
@Composable
private fun WalletViewPreview() {
  fun setShowTabBar(value: Boolean) {}
  WalletView(null, WalletViewViewModelMock(), ::setShowTabBar)
}