import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ethwalletapp.shared.navigation.Screen
import com.example.ethwalletapp.shared.theme.Gray21
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
    ) {
      Text(
        text = "Wallet Setup",
        fontSize = 40.sp,
        color = Color.White,
      )
      Spacer(modifier = Modifier.height(36.dp))
      Button(
        onClick = { /*TODO*/ },
        colors = ButtonDefaults.buttonColors(backgroundColor = Gray21),
        shape = RoundedCornerShape(50.dp),
        modifier = Modifier
          .height(56.dp)
          .padding(horizontal = 24.dp)
          .fillMaxWidth()
      ) {
        Text(
          text = "Import Using Recovery Phrase",
          fontSize = 16.sp,
          color = Color.White,
        )
      }
      Spacer(modifier = Modifier.height(16.dp))
      PrimaryButton(
        onClick = { navController?.navigate(Screen.CreateAccountScreen.route) },
        text = "Create a New Wallet"
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