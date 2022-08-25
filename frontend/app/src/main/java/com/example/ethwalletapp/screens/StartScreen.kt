import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethwalletapp.ui.theme.Gradient06
import com.example.ethwalletapp.ui.theme.Gray21
import com.example.ethwalletapp.ui.theme.Gray24


@Composable
fun StartScreen() {
  Scaffold(
    backgroundColor = Gray24
  ) {
    Column {
      Text(
        text = "Wallet Setup",
        fontSize = 56.sp,
        color = Color.White,
      )
      Button(
        onClick = { /*TODO*/ },
        colors = ButtonDefaults.buttonColors(backgroundColor = Gray21),
        shape = RoundedCornerShape(50.dp),
        modifier = Modifier
          .height(85.dp)
          .fillMaxWidth()
          .padding(horizontal = 24.dp, vertical = 16.dp)
      ) {
        Text(
          text = "Import Using Recovery Phrase",
          fontSize = 24.sp,
          color = Color.White,
        )
      }
      Button(
        onClick = { /*TODO*/ },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        shape = RoundedCornerShape(50.dp),
        modifier = Modifier
          .height(85.dp)
          .fillMaxWidth()
          .padding(horizontal = 24.dp, vertical = 16.dp)
      ) {
        Box(
          modifier = Modifier.background(Brush.horizontalGradient(colors = Gradient06))
        ) {
          Text(
            text = "Create a New Wallet",
            color = Color.White,
            modifier = Modifier
              .height(24.dp)
          )
        }
      }
    }
  }
}

@Preview
@Composable
fun PreviewStartScreen() {
  StartScreen()
}