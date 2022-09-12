package com.example.ethwalletapp.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ethwalletapp.presentation.settings.SettingsView
import com.example.ethwalletapp.presentation.wallet.WalletView
import com.example.ethwalletapp.presentation.wallet.WalletViewViewModel
import com.example.ethwalletapp.shared.theme.*
import com.google.accompanist.pager.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen() {
  Scaffold(
    backgroundColor = Gray24
  ) { padding ->
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    val views = listOf(
      "Wallet" to Icons.Outlined.Wallet,
      "Settings" to Icons.Outlined.Settings
    )

    Column(
      modifier = Modifier
        .padding(padding)
        .fillMaxSize()
        .padding(horizontal = 24.dp)
    ) {
      TabContent(views.size, pagerState, Modifier.weight(1f))
      TabBar(pagerState, scope, views)
    }
  }
}

@Preview
@Composable
private fun HomeScreenPreview() {
  HomeScreen()
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun TabContent(viewCount: Int, pagerState: PagerState, modifier: Modifier) {
  HorizontalPager(
    count = viewCount,
    state = pagerState,
    modifier = modifier
  ) { page ->
    when (page) {
      0 -> {
        val viewModel: WalletViewViewModel = hiltViewModel()
        WalletView(viewModel)
      }
      1 -> SettingsView()
    }
  }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun TabBar(
  pagerState: PagerState,
  scope: CoroutineScope,
  views: List<Pair<String, ImageVector>>,
) {
  TabRow(
    selectedTabIndex = pagerState.currentPage,
    backgroundColor = Gray24,
    indicator = {}
  ) {
    val gradientModifier = Modifier
      .graphicsLayer(alpha = 0.99f)
      .drawWithCache {
        val brush = Brush.horizontalGradient(Gradient06)
        onDrawWithContent {
          drawContent()
          drawRect(brush, blendMode = BlendMode.SrcAtop)
        }
      }

    views.forEachIndexed { index, _ ->
      Tab(
        selected = index == pagerState.currentPage,
        onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
        unselectedContentColor = Gray12,
        text = {
          Text(
            text = views[index].first,
            fontSize = 12.sp,
            modifier = if (index == pagerState.currentPage) gradientModifier else Modifier
          )
        },
        icon = {
          Icon(
            views[index].second,
            contentDescription = views[index].first,
            modifier = if (index == pagerState.currentPage) gradientModifier else Modifier
          )
        }
      )
    }
  }
}