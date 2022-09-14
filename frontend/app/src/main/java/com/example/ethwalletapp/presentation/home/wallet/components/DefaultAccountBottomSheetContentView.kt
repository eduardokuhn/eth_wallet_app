package com.example.ethwalletapp.presentation.home.wallet.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.ethwalletapp.shared.components.AppTextButton
import com.example.ethwalletapp.shared.theme.Gray12
import com.example.ethwalletapp.shared.theme.Green5
import com.example.ethwalletapp.shared.utils.Constant
import com.example.ethwalletapp.shared.utils.EthereumUnitConverter
import kotlinx.coroutines.launch
import org.kethereum.model.Address
import java.math.BigInteger

@Composable
fun DefaultAccountBottomSheetContentView(
  accounts: List<AccountEntry>?,
  setSelectedAccount: (account: AccountEntry) -> Unit,
  balances: List<BalanceEntry>?,
  onCreateNewAccount: () -> Unit,
  onImportAccount: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.padding(horizontal = 24.dp)
  ) {
    Box(Modifier.fillMaxWidth()) {
      Text(
        text = "Account",
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White,
        modifier = Modifier.align(Alignment.Center)
      )
    }
    if (accounts != null && accounts.isNotEmpty()) {
      LazyColumn {
        items(accounts) { account ->
          Spacer(Modifier.height(36.dp))
          AccountListItem(
            account = account,
            setSelectedAccount = setSelectedAccount,
            balance = balances?.singleOrNull { balance -> balance.address == account.address }
          )
        }
      }
    }
    Spacer(Modifier.height(36.dp))
    AppTextButton(onClick = onCreateNewAccount, text = "Create New Account")
    AppTextButton(onClick = onImportAccount, text = "Import Account")

  }
}

@Preview
@Composable
private fun DefaultAccountBottomSheetContentViewPreview() {
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
  DefaultAccountBottomSheetContentView(accounts, {}, balances, {}, {})
}

@Composable
fun AccountListItem(
  account: AccountEntry,
  setSelectedAccount: (account: AccountEntry) -> Unit,
  balance: BalanceEntry?
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .clickable { setSelectedAccount(account) }
      .fillMaxWidth()
  ) {
    Box(
      modifier = Modifier
        .size(40.dp)
        .background(Green5, CircleShape)
    ) {
      Text(
        text = (account.addressIndex + 1).toString(),
        fontSize = 18.sp,
        color = Color.White,
        modifier = Modifier.align(Alignment.Center)
      )
    }
    Spacer(Modifier.width(10.dp))
    Column() {
      Text(
        text = account.name,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White
      )
      Spacer(Modifier.height(6.dp))
      Text(
        text =
        if (balance != null) "${EthereumUnitConverter.weiToEther(balance.balance)} ETH"
        else "__.__ ETH",
        fontSize = 12.sp,
        color = Gray12
      )
    }
  }
}

@Preview
@Composable
private fun AccountListItemPreview() {
  AccountListItem(
    account = AccountEntry(
      address = Address("0x71C7656EC7ab88b098defB751B7401B5f6d8976F"),
      name = "Account 1",
      addressIndex = 0
    ),
    setSelectedAccount = {},
    balance = BalanceEntry(
      address = Address("0x71C7656EC7ab88b098defB751B7401B5f6d8976F"),
      tokenAddress = Address(Constant.ETHEREUM_TOKEN_ADDRESS),
      chainId = BigInteger.valueOf(0),
      balance = BigInteger.valueOf(1600),
    )
  )
}