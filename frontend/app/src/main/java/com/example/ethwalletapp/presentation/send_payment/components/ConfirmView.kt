package com.example.ethwalletapp.presentation.send_payment.components

import PrimaryButton
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethwalletapp.data.models.AccountEntry
import com.example.ethwalletapp.data.models.BalanceEntry
import com.example.ethwalletapp.shared.components.ErrorBanner
import com.example.ethwalletapp.shared.theme.*
import com.example.ethwalletapp.shared.utils.Constant
import com.example.ethwalletapp.shared.utils.ViewState
import org.kethereum.DEFAULT_GAS_LIMIT
import org.kethereum.model.Address
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

@Composable
fun ConfirmView(
  confirmViewState: ViewState,
  valueInputInEther: Double?,
  fromAccount: AccountEntry?,
  fromAccountBalance: BalanceEntry?,
  toAccount: AccountEntry?,
  networkFeeInEther: BigDecimal?,
  totalValueInEther: Double?,
  totalValueInUsd: Double?,
  onBack: () -> Unit,
  onClose: () -> Unit,
  onSend: () -> Unit
) {
  Column(
    Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())) {
    if (confirmViewState is ViewState.Error) {
      ErrorBanner(
        title = "Error! Retry it again",
        description = "There was an error sending the transaction"
      )
    }

    Spacer(Modifier.height(8.dp))
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp)
    ) {
      IconButton(
        onClick = onBack,
        modifier = Modifier.align(Alignment.CenterStart)
      ) {
        Icon(
          Icons.Outlined.KeyboardArrowLeft,
          contentDescription = "Back to amount",
          tint = Color.White
        )
      }
      Text(
        text = "Confirm",
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
    Spacer(Modifier.height(18.dp))
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier
        .padding(horizontal = 24.dp)
        .fillMaxSize()
    ) {
      Text(
        text = "Amount",
        fontSize = 14.sp,
        color = Color.White
      )
      Spacer(Modifier.height(14.dp))
      Text(
        text = "${valueInputInEther ?: "__.__"} ETH",
        fontSize = 40.sp,
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Clip,
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
      Spacer(Modifier.height(18.dp))
      Column {
        Text(
          text = "From",
          fontSize = 18.sp,
          fontWeight = FontWeight.SemiBold,
          color = Color.White
        )
        Spacer(Modifier.height(22.dp))
        AccountInput(
          selectedAccount = fromAccount,
          selectedAccountBalance = fromAccountBalance
        )
        Spacer(Modifier.height(22.dp))
        Text(
          text = "To",
          fontSize = 18.sp,
          fontWeight = FontWeight.SemiBold,
          color = Color.White
        )
        Spacer(Modifier.height(22.dp))
        AccountInput(
          selectedAccount = toAccount,
          selectedAccountBalance = null,
          showAddress = true
        )
        Spacer(Modifier.height(52.dp))
        Box(
          modifier = Modifier
            .background(
              color = Gray,
              shape = RoundedCornerShape(16.dp)
            )
        ) {
          Column {
            Column(Modifier.padding(16.dp)) {
              Row {
                Text(
                  text = "Amount",
                  fontSize = 13.sp,
                  color = Color.White
                )
                Spacer(Modifier.width(8.dp))
                Text(
                  text = "${valueInputInEther ?: "__.__"} ETH",
                  fontSize = 13.sp,
                  color = Color.White,
                  textAlign = TextAlign.End,
                  modifier = Modifier.fillMaxWidth()
                )
              }
              Spacer(Modifier.height(12.dp))
              Row {
                Text(
                  text = "Network fee",
                  fontSize = 13.sp,
                  color = Color.White
                )
                Spacer(Modifier.width(8.dp))
                Text(
                  text = "${networkFeeInEther ?: "__.__"} ETH",
                  fontSize = 13.sp,
                  color = Color.White,
                  textAlign = TextAlign.End,
                  modifier = Modifier.fillMaxWidth()
                )
              }
            }
            Divider(color = Gray22)
            Column(Modifier.padding(16.dp)) {
              Row {
                Text(
                  text = "Total Amount",
                  fontSize = 18.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = Color.White,
                )
                Spacer(Modifier.width(8.dp))
                Text(
                  text = "${totalValueInEther ?: "__.__"} ETH",
                  fontSize = 18.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = Color.White,
                  textAlign = TextAlign.End,
                  modifier = Modifier.fillMaxWidth()
                )
              }
              Spacer(Modifier.height(12.dp))
              Text(
                text = "$${totalValueInUsd?.toBigDecimal()?.setScale(2, RoundingMode.HALF_EVEN) ?: "__,__"}",
                fontSize = 12.sp,
                color = Gray12,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
              )
            }
          }
        }
        Spacer(
          Modifier
            .height(52.dp)
            .weight(1f))
        PrimaryButton(
          onClick = { if (confirmViewState !is ViewState.Loading) onSend() },
          text = "Send",
          isLoading = confirmViewState is ViewState.Loading
        )
        Spacer(Modifier.height(42.dp))
      }
    }
  }
}

@Preview
@Composable
private fun ConfirmViewPreview() {
  ConfirmView(
    confirmViewState = ViewState.Loading,
    valueInputInEther = 0.0,
    fromAccount = AccountEntry(
      address = Address("0x71C7656EC7ab88b098defB751B7401B5f6d8976F"),
      name = "Account 1",
      addressIndex = 0
    ),
    fromAccountBalance = BalanceEntry(
      address = Address("0x71C7656EC7ab88b098defB751B7401B5f6d8976F"),
      tokenAddress = Address(Constant.ETHEREUM_TOKEN_ADDRESS),
      chainId = BigInteger.valueOf(0),
      balance = BigInteger.valueOf(0)
    ),
    toAccount = AccountEntry(
      address = Address("0x71C7656EC7ab88b398defB751B7401B5f6d8976F"),
      name = "Account 2",
      addressIndex = 0
    ),
    networkFeeInEther =  BigDecimal("0"),
    totalValueInEther = 2.0,
    totalValueInUsd = 100.0,
    onBack = {},
    onClose = {},
    onSend = {}
  )
}