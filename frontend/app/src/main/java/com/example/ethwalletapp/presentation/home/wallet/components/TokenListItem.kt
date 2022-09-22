package com.example.ethwalletapp.presentation.home.wallet.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethwalletapp.R
import com.example.ethwalletapp.shared.theme.Gray12
import com.example.ethwalletapp.shared.utils.weiToEther
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

@Composable
fun TokenListItem(
  icon: Painter,
  name: String,
  abbr: String,
  tokenPrice: Double?,
  balance: BigInteger?,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .clickable { onClick() }
      .fillMaxWidth()
  ) {
    Image(
      painter = icon,
      contentDescription = "Ethereum Logo",
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .size(32.dp)
        .clip(CircleShape)
    )
    Spacer(Modifier.width(10.dp))
    Column {
      Text(
        text = name,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White
      )
      Spacer(Modifier.height(8.dp))
      Text(
        text =
          "$" + if (tokenPrice != null && balance != null) "${BigDecimal(tokenPrice).times(balance.weiToEther()).setScale(2, RoundingMode.HALF_EVEN)}"
          else "$" + "__,__",
        fontSize = 12.sp,
        color = Gray12
      )
    }
    Spacer(Modifier.weight(1f))
    Text(
      text = "${balance?.weiToEther() ?: "__.__"} $abbr",
      fontSize = 18.sp,
      fontWeight = FontWeight.SemiBold,
      color = Color.White
    )
  }
}

@Preview
@Composable
private fun TokenListItemPreview() {
  TokenListItem(
    icon = painterResource(R.drawable.ic_ethereum),
    name = "Ethereum",
    abbr = "ETH",
    tokenPrice = 1600.00,
    balance = BigInteger.valueOf(8),
    onClick = {}
  )
}