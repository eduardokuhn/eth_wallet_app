package com.example.ethwalletapp.presentation.send_payment.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethwalletapp.data.models.AccountEntry
import com.example.ethwalletapp.data.models.BalanceEntry
import com.example.ethwalletapp.shared.components.AccountListItem
import com.example.ethwalletapp.shared.utils.Constant
import org.kethereum.model.Address
import java.math.BigInteger

@Composable
fun SendToViewBottomSheetContentBody(
  accounts: List<AccountEntry>?,
  onSelect: (account: AccountEntry, balance: BalanceEntry?) -> Unit,
  balances: List<BalanceEntry>?,
  isAccountSelected: (account: AccountEntry) -> Boolean
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.padding(horizontal = 24.dp)
  ) {
    Spacer(Modifier.height(16.dp))
    Text(
      text = "Accounts",
      fontSize = 18.sp,
      fontWeight = FontWeight.SemiBold,
      color = Color.White,
    )
    Spacer(Modifier.height(50.dp))
    LazyColumn(Modifier.weight(1f)) {
      if (accounts != null && accounts.isNotEmpty()) {
        itemsIndexed(accounts) { index, account ->
          AccountListItem(
            account = account,
            onSelect = onSelect,
            balance = balances?.firstOrNull { balance -> balance.address == account.address },
            showAddress = true,
            isAccountSelected = isAccountSelected
          )
          if (index < accounts.lastIndex) Spacer(Modifier.height(36.dp))
        }
      }
    }
    Spacer(Modifier.height(42.dp))
  }
}

@Preview
@Composable
private fun SendToViewBottomSheetContentBodyPreview() {
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
  SendToViewBottomSheetContentBody(
    accounts = accounts,
    onSelect = { account, balance -> println("$account $balance") },
    balances = balances,
    isAccountSelected = { true }
  )
}