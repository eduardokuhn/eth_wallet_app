import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ethwalletapp.shared.components.SecondaryButton
import com.example.ethwalletapp.shared.navigation.Screen
import com.example.ethwalletapp.shared.theme.Gray24


@Composable
fun StartScreen(navController: NavController?) {
  Scaffold(
    backgroundColor = Gray24
  ) { padding ->
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Bottom,
      modifier = Modifier
        .padding(padding)
        .fillMaxSize()
        .padding(horizontal = 24.dp)
    ) {
      Text(
        text = "Wallet Setup",
        fontWeight = FontWeight.SemiBold,
        fontSize = 40.sp,
        color = Color.White,
      )
      Spacer(modifier = Modifier.height(36.dp))
      SecondaryButton(
        onClick = { navController?.navigate(Screen.ImportWalletScreen.route) },
        text = "Import Using Recovery Phrase",
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(modifier = Modifier.height(16.dp))
      PrimaryButton(
        onClick = { navController?.navigate(Screen.CreateWalletScreen.route) },
        text = "Create a New Wallet",
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(modifier = Modifier.height(66.dp))
    }
  }
}

@Preview
@Composable
fun StartScreenPreview() {
  StartScreen(null)
}