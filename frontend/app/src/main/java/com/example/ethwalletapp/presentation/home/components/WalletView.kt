package com.example.ethwalletapp.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.ChangeCircle
import androidx.compose.material.icons.outlined.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethwalletapp.R
import com.example.ethwalletapp.shared.components.SecondaryButton
import com.example.ethwalletapp.shared.theme.Gradient07
import com.example.ethwalletapp.shared.theme.Green5
import com.example.ethwalletapp.shared.theme.Primary5
import java.math.BigInteger

@Composable
fun WalletView() {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.fillMaxSize()
  ) {
    Spacer(Modifier.height(44.dp))
    Box(Modifier.fillMaxWidth()) {
      Box(
        modifier = Modifier
          .clickable { }
          .size(36.dp)
          .align(Alignment.CenterStart)
          .background(Green5, CircleShape)
      ) {
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
          .clickable { }
          .align(Alignment.Center)
      ) {
        Text(
          text = "Network Name",
          fontSize = 13.sp,
          color = Color.White
        )
        Spacer(Modifier.width(4.dp))
        Icon(
          Icons.Outlined.ArrowDropDown,
          contentDescription = "Switch network",
          tint = Color.White
        )
      }
    }
    Spacer(Modifier.height(48.dp))
    Text(
      text = "9.2362 ETH",
      fontSize = 40.sp,
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
      text = "$16,868.00",
      fontSize = 15.sp,
      color = Color.White
    )
    Spacer(Modifier.height(28.dp))
    Row {
      SecondaryButton(
        onClick = { /*TODO*/ },
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
        onClick = { /*TODO*/ },
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
      color = Color.White,
      modifier = Modifier.align(Alignment.Start)
    )
    Spacer(Modifier.height(28.dp))
    TokenListItem(
      icon = painterResource(R.drawable.ic_ethereum),
      name = "Ethereum",
      abbr = "ETH",
      value = BigInteger.valueOf(1600),
      balance = BigInteger.valueOf(8)
    )
  }
}

@Preview
@Composable
private fun WalletViewPreview() {
  WalletView()
}