package com.example.ethwalletapp.presentation.home.wallet.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethwalletapp.data.services.EthereumNetwork
import com.example.ethwalletapp.shared.theme.Green5

@Composable
fun NetworkBottomSheetContent(
  selectNetwork: (network: EthereumNetwork) -> Unit,
  isNetworkSelected: (network: EthereumNetwork) -> Boolean
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.padding(horizontal = 24.dp)
  ) {
    Spacer(Modifier.height(16.dp))
    Text(
      text = "Network",
      fontSize = 18.sp,
      fontWeight = FontWeight.SemiBold,
      color = Color.White
    )
    Spacer(Modifier.height(34.dp))
    Column(Modifier.weight(1f)) {
      EthereumNetwork.values().forEach { network ->
        NetworkListItem(
          ethereumNetwork = network,
          selectNetwork = selectNetwork,
          isNetworkSelected = isNetworkSelected
        )
      }
    }
  }
}

@Preview
@Composable
private fun NetworkBottomSheetContentPreview() {
  NetworkBottomSheetContent(
    selectNetwork = {},
    isNetworkSelected = {true}
  )
}

@Composable
fun NetworkListItem(
  ethereumNetwork: EthereumNetwork,
  selectNetwork: (network: EthereumNetwork) -> Unit,
  isNetworkSelected: (network: EthereumNetwork) -> Boolean
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .clickable { selectNetwork(ethereumNetwork) }
      // TODO fix it: expading when selected
      .padding(vertical = 15.dp)
      .fillMaxWidth()
  ) {
    Box(
      modifier = Modifier
        .size(12.dp)
        .background(ethereumNetwork.color(), CircleShape)
    )
    Spacer(Modifier.width(18.dp))
    Text(
      text = ethereumNetwork.toString(),
      fontSize = 15.sp,
      color = Color.White
    )
    Spacer(Modifier.weight(1f))
    if (isNetworkSelected(ethereumNetwork)) {
      Icon(
        Icons.Outlined.Check,
        contentDescription = "Network is selected",
        tint = Green5
      )
    }
  }
}

@Preview
@Composable
private fun NetworkListItemPreview() {
  NetworkListItem(
    ethereumNetwork = EthereumNetwork.Rinkeby,
    selectNetwork = {},
    isNetworkSelected = {true}
  )
}