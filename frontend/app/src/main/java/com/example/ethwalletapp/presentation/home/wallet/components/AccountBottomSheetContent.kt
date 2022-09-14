package com.example.ethwalletapp.presentation.home.wallet.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import com.example.ethwalletapp.data.models.AccountEntry
import com.example.ethwalletapp.data.models.BalanceEntry
import com.example.ethwalletapp.shared.utils.Constant
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import org.kethereum.model.Address
import java.math.BigInteger

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AccountBottomSheetContent(
  accounts: List<AccountEntry>?,
  setSelectedAccount: (account: AccountEntry) -> Unit,
  balances: List<BalanceEntry>?,
  newAccountName: String,
  setNewAccountName: (value: String) -> Unit,
  createNewAccount: () -> Unit,
  createNewAccountHasError: Boolean,
  importAccount: () -> Unit
) {
  val pagerState = rememberPagerState()
  val scope = rememberCoroutineScope()

  HorizontalPager(
    count = 3,
    state = pagerState,
    userScrollEnabled = false
  ) { page ->
    when (page) {
      0 -> DefaultAccountBottomSheetContentView(
        accounts = accounts,
        setSelectedAccount = setSelectedAccount,
        balances = balances,
        onCreateNewAccount = { scope.launch { pagerState.animateScrollToPage(1) } },
        onImportAccount = { scope.launch { pagerState.animateScrollToPage(2) } }
      )
      1 -> CreateAccountBottomSheetContentView(
        onBack = { scope.launch{ pagerState.animateScrollToPage(0) } },
        newAccountName = newAccountName,
        setNewAccountName = setNewAccountName,
        onCreate = createNewAccount,
        onCreateHasError = createNewAccountHasError
      )
      2 -> ImportAccountBottomSheetContentView()
    }
  }
}

@Preview
@Composable
private fun AccountBottomSheetContentPreview() {
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
  AccountBottomSheetContent(
    accounts = accounts,
    setSelectedAccount = {},
    balances = balances,
    newAccountName = "",
    setNewAccountName = {},
    createNewAccount = {},
    createNewAccountHasError = false,
    importAccount = {}
  )
}
