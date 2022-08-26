import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethwalletapp.ui.theme.Gradient06

@Composable
fun GradientButton(
  onClick: () -> Unit,
  text: String,
) {
  Button(
    onClick = onClick,
    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
    shape = RoundedCornerShape(50.dp),
    contentPadding = PaddingValues(0.dp),
    modifier = Modifier
      .height(56.dp)
      .padding(horizontal = 24.dp)
      .fillMaxWidth()
  ) {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .background(Brush.horizontalGradient(colors = Gradient06))
        .fillMaxSize()
    ) {
      Text(
        text = text,
        fontSize = 16.sp,
        color = Color.White,
      )
    }
  }
}