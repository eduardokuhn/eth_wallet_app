package com.example.ethwalletapp.presentation.send_payment.components

import PrimaryButton
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethwalletapp.data.models.BalanceEntry
import com.example.ethwalletapp.shared.components.ErrorBanner
import com.example.ethwalletapp.shared.components.SecondaryButton
import com.example.ethwalletapp.shared.theme.Blue5
import com.example.ethwalletapp.shared.theme.Gradient07
import com.example.ethwalletapp.shared.theme.Gray22
import com.example.ethwalletapp.shared.utils.Constant
import com.example.ethwalletapp.shared.utils.EthereumUnitConverter
import org.kethereum.model.Address
import java.math.BigInteger

@Composable
fun AmountView(
  hasSufficientFunds: Boolean,
  valueInputInEther: Double?,
  valueInputInUsd: Double?,
  valueInput: String,
  setValueInput: (value: String) -> Unit,
  isValueInputInUsd: Boolean,
  toggleIsValueInputInUsd: () -> Unit,
  fromAccountBalance: BalanceEntry?,
  useMax: () -> Unit,
  onBack: () -> Unit,
  onClose: () -> Unit,
  onNext: () -> Unit
) {
  Column(Modifier.fillMaxSize()) {
    if (!hasSufficientFunds) {
      ErrorBanner(
        title = "Insufficient funds!",
        description = "Retry another value"
      )
    }

    Spacer(Modifier.height(8.dp))
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp)
    ) {
      IconButton(
        onClick =  onBack,
        modifier = Modifier.align(Alignment.CenterStart)
      ) {
        Icon(
          Icons.Outlined.KeyboardArrowLeft,
          contentDescription = "Back to send to",
          tint = Color.White
        )
      }
      Text(
        text = "Amount",
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
      Box(Modifier.fillMaxWidth()) {
        Box(
          modifier = Modifier
            .border(
              width = 1.dp,
              color = Gray22,
              shape = RoundedCornerShape(16.dp)
            )
            .background(
              color = Color.Transparent,
              shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .align(Alignment.Center)
        ) {
          Text(
            text = "ETH",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
          )
        }
        TextButton(
          onClick = useMax,
          modifier = Modifier.align(Alignment.CenterEnd)
        ) {
          Text(
            text = "Use Max",
            fontSize = 16.sp,
            color = Blue5
          )
        }
      }
      Spacer(Modifier.height(40.dp))
      BasicTextField(
        value = valueInput,
        onValueChange = setValueInput,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = TextStyle(fontSize = 0.sp),
        decorationBox = { innerTextField ->
          Text(
            text = valueInput,
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
          innerTextField()
        },
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(Modifier.height(26.dp))
      SecondaryButton(
        onClick = toggleIsValueInputInUsd,
        text = if (!isValueInputInUsd) "$${valueInputInUsd ?: "__,__"}" else "${valueInputInEther ?: "__.__"} ETH",
        fontWeight = FontWeight.Normal,
        trailingIcon = {
          Icon(
            Icons.Outlined.Repeat,
            contentDescription = if (!isValueInputInUsd) "Change input to ETH" else "Change input to USD",
            tint = Color.White
          )
        },
        modifier = Modifier.height(38.dp)
      )
      Spacer(Modifier.height(20.dp))
      Text(
        text = "Balance: ${EthereumUnitConverter.weiToEther(fromAccountBalance?.balance ?: BigInteger.valueOf(0))} ETH",
        fontSize = 16.sp,
        color = Color.White
      )
      Spacer(Modifier.weight(1f))
      PrimaryButton(
        onClick = onNext,
        text = "Next",
        disabled = !hasSufficientFunds
      )
      Spacer(Modifier.height(42.dp))
    }
  }
}

@Preview
@Composable
private fun AmountViewPreview() {
  AmountView(
    hasSufficientFunds = false,
    valueInputInEther = 0.0,
    valueInputInUsd = 0.0,
    valueInput = "0",
    setValueInput = {},
    isValueInputInUsd = true,
    toggleIsValueInputInUsd = {},
    fromAccountBalance = BalanceEntry(
      address = Address("0x71C7656EC7ab88b098defB751B7401B5f6d8976F"),
      tokenAddress = Address(Constant.ETHEREUM_TOKEN_ADDRESS),
      chainId = BigInteger.valueOf(0),
      balance = BigInteger.valueOf(0)
    ),
    useMax = {},
    onBack = {},
    onClose = {},
    onNext = {}
  )
}