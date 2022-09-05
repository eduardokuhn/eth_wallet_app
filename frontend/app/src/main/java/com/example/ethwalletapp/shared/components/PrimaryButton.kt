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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethwalletapp.shared.theme.Gradient06
import com.example.ethwalletapp.shared.theme.Gray18
import com.example.ethwalletapp.shared.theme.Gray23

@Composable
fun PrimaryButton(
  onClick: () -> Unit,
  text: String,
  disabled: Boolean = false
) {
  Button(
    onClick = { if (!disabled) onClick() },
    colors = ButtonDefaults.buttonColors(backgroundColor = if (!disabled) Color.Transparent else Gray23),
    shape = RoundedCornerShape(50.dp),
    contentPadding = PaddingValues(0.dp),
    modifier = Modifier
      .height(56.dp)
      .padding(horizontal = 24.dp)
      .fillMaxWidth()
  ) {
    if (!disabled)
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
    else
      Text(
        text = text,
        fontSize = 16.sp,
        color = Gray18,
      )
  }
}

@Preview
@Composable
fun PrimaryButtonPreview() {
  PrimaryButton(onClick = {}, text = "Enter")
}