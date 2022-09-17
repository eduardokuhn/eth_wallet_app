package com.example.ethwalletapp.presentation.send_payment.components

import PrimaryButton
import TextInput
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethwalletapp.data.models.AccountEntry
import com.example.ethwalletapp.data.models.BalanceEntry
import com.example.ethwalletapp.shared.components.AppTextButton
import com.example.ethwalletapp.shared.theme.Gray12
import com.example.ethwalletapp.shared.theme.Gray24
import com.example.ethwalletapp.shared.theme.Green5
import com.example.ethwalletapp.shared.utils.Constant
import com.example.ethwalletapp.shared.utils.EthereumUnitConverter
import kotlinx.coroutines.launch
import org.kethereum.model.Address
import java.math.BigInteger

sealed class SendToViewBottomSheetContent() {
  object FromAccountBottomSheet: SendToViewBottomSheetContent()
  object ToAccountBottomSheet: SendToViewBottomSheetContent()
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SendToView(
  accounts: List<AccountEntry>?,
  fromAccount: AccountEntry?,
  setFromAccount: (account: AccountEntry, balance: BalanceEntry?) -> Unit,
  isFromAccountSelected: (account: AccountEntry) -> Boolean,
  balances: List<BalanceEntry>?,
  fromAccountBalance: BalanceEntry?,
  toAccountInput: String,
  setToAccountInput: (value: String) -> Unit,
  toAccountInputHelperText: String,
  isToAccountInputValid: Boolean,
  toOwnAccount: AccountEntry?,
  setToOwnAccount: (account: AccountEntry?, balance: BalanceEntry?) -> Unit,
  isToOwnAccountSelected: (account: AccountEntry) -> Boolean,
  onNext: () -> Unit
) {
  var currentBottomSheetContent: SendToViewBottomSheetContent? by remember {
    mutableStateOf(null)
  }
  val sheetState = rememberModalBottomSheetState(
    initialValue = ModalBottomSheetValue.Hidden,
    confirmStateChange = {
      if (it == ModalBottomSheetValue.Hidden) currentBottomSheetContent = null
      it != ModalBottomSheetValue.HalfExpanded
    }
  )
  val scope = rememberCoroutineScope()

  fun openSheet(content: SendToViewBottomSheetContent) {
    currentBottomSheetContent = content
    scope.launch { sheetState.show() }
  }
  
  ModalBottomSheetLayout(
    sheetState = sheetState,
    sheetContent = {
      // To avoid crash
      Spacer(Modifier.height(1.dp))
      currentBottomSheetContent?.let { currentSheet ->
        when (currentSheet) {
          SendToViewBottomSheetContent.FromAccountBottomSheet -> SendToViewBottomSheetContentBody(
            accounts = accounts,
            onSelect = setFromAccount,
            balances = balances,
            isAccountSelected = isFromAccountSelected
          )
          SendToViewBottomSheetContent.ToAccountBottomSheet -> SendToViewBottomSheetContentBody(
            accounts = accounts,
            onSelect = setToOwnAccount,
            balances = null,
            isAccountSelected = isToOwnAccountSelected
          )
        }
      }
    },
    scrimColor = Color.Black.copy(alpha = 0.65f),
    sheetBackgroundColor = Gray24
  ) {
    Column(
      modifier = Modifier
        .padding(horizontal = 24.dp)
        .fillMaxSize()
    ) {
      Spacer(Modifier.height(8.dp))
      Box(Modifier.fillMaxWidth()) {
        Text(
          text = "Send to",
          fontSize = 18.sp,
          fontWeight = FontWeight.SemiBold,
          color = Color.White,
          modifier = Modifier.align(Alignment.Center)
        )
        IconButton(
          onClick = { /*TODO*/ },
          modifier = Modifier.align(Alignment.CenterEnd)
        ) {
          Icon(
            Icons.Outlined.Close,
            contentDescription = "Close screen",
            tint = Color.White
          )
        }
      }
      Spacer(Modifier.height(24.dp))
      Text(
        text = "From",
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White
      )
      Spacer(Modifier.height(22.dp))
      AccountInput(
        selectedAccount = fromAccount,
        selectedAccountBalance = fromAccountBalance,
        onClick = { openSheet(SendToViewBottomSheetContent.FromAccountBottomSheet) },
        trailingIcon = {
          Icon(
            Icons.Outlined.KeyboardArrowRight,
            contentDescription = "Open accounts sheet",
            tint = Color.White
          )
        }
      )
      Spacer(Modifier.height(22.dp))
      Text(
        text = "To",
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White
      )
      Spacer(Modifier.height(22.dp))
      if (toOwnAccount != null) {
        AccountInput(
          selectedAccount = toOwnAccount,
          selectedAccountBalance = null,
          showAddress = true,
          onClick = { setToOwnAccount(null, null) },
          trailingIcon = {
            Icon(
              Icons.Outlined.Close,
              contentDescription = "Remove to own account",
              tint = Color.White
            )
          }
        )
      }
      TextInput(
        value = toAccountInput,
        onValueChange = setToAccountInput,
        label = "Public address (0x)",
        helperText = toAccountInputHelperText,
        hasError = !isToAccountInputValid
      )
      AppTextButton(
        onClick = { openSheet(SendToViewBottomSheetContent.ToAccountBottomSheet) },
        text = "Transfer Between My Accounts",
        textStyle = TextStyle(textDecoration = TextDecoration.Underline)
      )
      Spacer(Modifier.weight(1f))
      PrimaryButton(
        onClick = onNext,
        text = "Next",
        disabled = !isToAccountInputValid || toOwnAccount != null,
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(Modifier.height(42.dp))
    }
  }
}

@Composable
@Preview
private fun SendToViewPreview() {
  val accounts = listOf(
    AccountEntry(
      address = Address("0x71C7656EC7ab88b098defB751B7401B5f6d8976F"),
      name = "Account 1",
      addressIndex = 0
    ),
    AccountEntry(
      address = Address("0x71C7656EC7ab88b098defB751B7401B5f6d8976F"),
      name = "Account 1",
      addressIndex = 0
    ),
    AccountEntry(
      address = Address("0x71C7656EC7ab88b098defB751B7401B5f6d8976F"),
      name = "Account 1",
      addressIndex = 0
    ),
    AccountEntry(
      address = Address("0x71C7656EC7ab88b098defB751B7401B5f6d8976F"),
      name = "Account 1",
      addressIndex = 0
    ),
    AccountEntry(
      address = Address("0x71C7656EC7ab88b098defB751B7401B5f6d8976F"),
      name = "Account 1",
      addressIndex = 0
    ),
    AccountEntry(
      address = Address("0x71C7656EC7ab88b098defB751B7401B5f6d8976F"),
      name = "Account 1",
      addressIndex = 0
    )
  )
  val balances = listOf(
    BalanceEntry(
      address = Address("0x71C7656EC7ab88b098defB751B7401B5f6d8976F"),
      tokenAddress = Address(Constant.ETHEREUM_TOKEN_ADDRESS),
      chainId = BigInteger.valueOf(0),
      balance = BigInteger.valueOf(0)
    ),
    BalanceEntry(
      address = Address("0x71C7656EC7ab88b098defB751B7401B5f6d8976F"),
      tokenAddress = Address(Constant.ETHEREUM_TOKEN_ADDRESS),
      chainId = BigInteger.valueOf(0),
      balance = BigInteger.valueOf(0)
    ),
    BalanceEntry(
      address = Address("0x71C7656EC7ab88b098defB751B7401B5f6d8976F"),
      tokenAddress = Address(Constant.ETHEREUM_TOKEN_ADDRESS),
      chainId = BigInteger.valueOf(0),
      balance = BigInteger.valueOf(0)
    ),
    BalanceEntry(
      address = Address("0x71C7656EC7ab88b098defB751B7401B5f6d8976F"),
      tokenAddress = Address(Constant.ETHEREUM_TOKEN_ADDRESS),
      chainId = BigInteger.valueOf(0),
      balance = BigInteger.valueOf(0)
    ),
    BalanceEntry(
      address = Address("0x71C7656EC7ab88b098defB751B7401B5f6d8976F"),
      tokenAddress = Address(Constant.ETHEREUM_TOKEN_ADDRESS),
      chainId = BigInteger.valueOf(0),
      balance = BigInteger.valueOf(0)
    ),
    BalanceEntry(
      address = Address("0x71C7656EC7ab88b098defB751B7401B5f6d8976F"),
      tokenAddress = Address(Constant.ETHEREUM_TOKEN_ADDRESS),
      chainId = BigInteger.valueOf(0),
      balance = BigInteger.valueOf(0)
    )
  )
  SendToView(
    accounts = accounts,
    fromAccount = accounts[0],
    balances = balances,
    fromAccountBalance = balances[0],
    toAccountInput = "",
    setToAccountInput = {},
    toAccountInputHelperText = "",
    isToAccountInputValid = true,
    toOwnAccount = null,
    setToOwnAccount = { account, balance -> print("$account $balance") },
    isToOwnAccountSelected = { true },
    onNext = {},
    isFromAccountSelected = { true },
    setFromAccount = { account, balance -> print("$account $balance") }
  )
}

@Composable
private fun AccountInput(
  selectedAccount: AccountEntry?,
  selectedAccountBalance: BalanceEntry?,
  showAddress: Boolean = false,
  onClick: () -> Unit,
  trailingIcon: @Composable () -> Unit
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .clickable { onClick() }
      .fillMaxWidth()
  ) {
    Box(
      modifier = Modifier
        .size(40.dp)
        .background(Green5, CircleShape)
    ) {
      Text(
        text = if (selectedAccount != null) "${selectedAccount.addressIndex + 1}" else "?",
        fontSize = 18.sp,
        color = Color.White,
        modifier = Modifier.align(Alignment.Center)
      )
    }
    Spacer(Modifier.width(16.dp))
    Column(Modifier.weight(1f)) {
      Text(
        text = selectedAccount?.name ?: "?",
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White
      )
      Spacer(Modifier.height(6.dp))
      Text(
        text =
          if (showAddress && selectedAccount != null) selectedAccount.address.hex
          else {
            if (selectedAccountBalance != null) "Balance: ${EthereumUnitConverter.weiToEther(selectedAccountBalance.balance)} ETH"
            else "Balance: __.__ ETH"
          },
        fontSize = 12.sp,
        color = Gray12
      )
    }
    trailingIcon.invoke()
  }
}