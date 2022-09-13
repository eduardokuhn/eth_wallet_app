package com.example.ethwalletapp.presentation.home.wallet.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.ethwalletapp.shared.components.AppTextButton
import com.example.ethwalletapp.shared.theme.Gray12
import com.example.ethwalletapp.shared.theme.Green5
import com.example.ethwalletapp.shared.utils.Constant
import com.example.ethwalletapp.shared.utils.EthereumUnitConverter
import org.kethereum.model.Address
import java.math.BigInteger

@Composable
fun AccountBottomSheet(
  accounts: List<AccountEntry>?,
  setSelectedAccount: (account: AccountEntry) -> Unit,
  balances: List<BalanceEntry>?,
  ethUsdPrice: Double?,
  createNewAccount: () -> Unit,
  importAccount: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .padding(horizontal = 24.dp)
      .fillMaxWidth()
  ) {
    Spacer(Modifier.height(15.dp))
    Text(
      text = "Account",
      fontSize = 18.sp,
      fontWeight = FontWeight.SemiBold,
      color = Color.White
    )
    Spacer(Modifier.height(15.dp))
    if (accounts != null && accounts.isNotEmpty()) {
      LazyColumn {
        items(accounts) { account ->
          Spacer(Modifier.height(36.dp))
          AccountListItem(
            account = account,
            ethUsdPrice = 1600.0
          )
        }
      }
    }
    Spacer(Modifier.height(36.dp))
    AppTextButton(
      onClick = { /*TODO*/ },
      text = "Create New Account"
    )
    AppTextButton(
      onClick = { /*TODO*/ },
      text = "Import Account"
    )
    Spacer(modifier = Modifier.height(36.dp))
  }
}

@Preview
@Composable
private fun AccountBottomSheetPreview() {
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
    ), AccountEntry(
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
    )
  )
  AccountBottomSheet(accounts, {}, balances, 0.0, {}, {})
}

@Composable
fun AccountListItem(
  account: AccountEntry,
  ethUsdPrice: Double
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier.fillMaxWidth()
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
        text = "$ethUsdPrice ETH",
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
    ethUsdPrice = 1600.0
  )
}