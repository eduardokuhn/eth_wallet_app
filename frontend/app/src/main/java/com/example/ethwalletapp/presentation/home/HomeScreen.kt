package com.example.ethwalletapp.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ethwalletapp.presentation.home.wallet.WalletView
import com.example.ethwalletapp.presentation.home.wallet.WalletViewViewModel
import com.example.ethwalletapp.presentation.settings.SettingsView
import com.example.ethwalletapp.presentation.settings.SettingsViewViewModel
import com.example.ethwalletapp.shared.theme.Gradient06
import com.example.ethwalletapp.shared.theme.Gray12
import com.example.ethwalletapp.shared.theme.Gray24
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
  navController: NavController?
) {
  Scaffold(
    backgroundColor = Gray24
  ) { padding ->
    val pagerState = rememberPagerState()
    var showTabBar = rememberSaveable { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    val views = listOf(
      "Wallet" to Icons.Outlined.Wallet,
      "Settings" to Icons.Outlined.Settings
    )

    fun setShowTabBar(value: Boolean) { showTabBar.value = value }

    Column(
      modifier = Modifier
        .padding(padding)
        .fillMaxSize()
    ) {
      TabContent(
        viewCount = views.size,
        pagerState = pagerState,
        setShowTabBar = ::setShowTabBar,
        navController = navController
      )
      if (showTabBar.value) TabBar(pagerState, scope, views)
    }
  }
}

@Preview
@Composable
private fun HomeScreenPreview() {
  HomeScreen(null)
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ColumnScope.TabContent(
  viewCount: Int,
  pagerState: PagerState,
  setShowTabBar: (value: Boolean) -> Unit,
  navController: NavController?
) {
  HorizontalPager(
    count = viewCount,
    state = pagerState,
    modifier = Modifier.weight(1f)
  ) { page ->
    when (page) {
      0 -> {
        val viewModel: WalletViewViewModel = hiltViewModel()
        WalletView(navController, viewModel, setShowTabBar)
      }
      1 -> {
        val viewModel: SettingsViewViewModel = hiltViewModel()
        SettingsView(navController, viewModel)
      }
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