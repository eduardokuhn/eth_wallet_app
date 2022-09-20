package com.example.ethwalletapp.presentation.send_payment.components

import PrimaryButton
import TextInput
import androidx.compose.foundation.layout.*
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
import com.example.ethwalletapp.shared.theme.Gray24
import com.example.ethwalletapp.shared.utils.Constant
import kotlinx.coroutines.launch
import org.kethereum.model.Address
import java.math.BigInteger

sealed class SendToViewBottomSheetContent {
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
  toAccountInputHelperText: String?,
  isToAccountInputValid: Boolean,
  toAccount: AccountEntry?,
  setToAccount: (account: AccountEntry?, balance: BalanceEntry?) -> Unit,
  isToAccountSelected: (account: AccountEntry) -> Boolean,
  onNext: () -> Unit,
  validateToAccountInput: () -> Boolean,
  onClose: () -> Unit,
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
            onSelect = setToAccount,
            balances = null,
            isAccountSelected = isToAccountSelected
          )
        }
      }
    },
    scrimColor = Color.Black.copy(alpha = 0.65f),
    sheetBackgroundColor = Gray24
  ) {
    Column(
      modifier = Modifier.fillMaxSize()
    ) {
      Spacer(Modifier.height(8.dp))
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 10.dp)
      ) {
        Text(
          text = "Send to",
          fontSize = 18.sp,
          fontWeight = FontWeight.SemiBold,
          color = Color.White,
          modifier = Modifier.align(Alignment.Center)
        )
        IconButton(
          onClick =  onClose,
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
      Column(
        modifier = Modifier
          .padding(horizontal = 24.dp)
          .fillMaxSize()
      ) {
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
        if (toAccount != null) {
          AccountInput(
            selectedAccount = toAccount,
            selectedAccountBalance = null,
            showAddress = true,
            onClick = { setToAccount(null, null) },
            trailingIcon = {
              Icon(
                Icons.Outlined.Close,
                contentDescription = "Remove to own account",
                tint = Color.White
              )
            }
          )
        } else {
          TextInput(
            value = toAccountInput,
            onValueChange = setToAccountInput,
            label = "Public address (0x)",
            helperText = toAccountInputHelperText,
            hasError = !isToAccountInputValid
          )
        }
        AppTextButton(
          onClick = { openSheet(SendToViewBottomSheetContent.ToAccountBottomSheet) },
          text = "Transfer Between My Accounts",
          textStyle = TextStyle(textDecoration = TextDecoration.Underline)
        )
        Spacer(Modifier.weight(1f))
        PrimaryButton(
          onClick = {
            val ok = validateToAccountInput()
            if (ok) onNext()
          },
          text = "Next",
          disabled = !isToAccountInputValid,
          modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(42.dp))
      }
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
    onClose = {},
    accounts = accounts,
    fromAccount = accounts[0],
    balances = balances,
    fromAccountBalance = balances[0],
    toAccountInput = "",
    setToAccountInput = {},
    toAccountInputHelperText = "",
    isToAccountInputValid = true,
    toAccount = null,
    setToAccount = { account, balance -> print("$account $balance") },
    isToAccountSelected = { true },
    onNext = {},
    isFromAccountSelected = { true },
    setFromAccount = { account, balance -> print("$account $balance") },
    validateToAccountInput = {true}
  )
}