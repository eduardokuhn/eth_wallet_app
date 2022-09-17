import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
  disabled: Boolean = false,
  isLoading: Boolean = false,
  modifier: Modifier = Modifier
) {
  Button(
    onClick = { if (!disabled && !isLoading) onClick() },
    shape = RoundedCornerShape(50.dp),
    colors = ButtonDefaults.buttonColors(backgroundColor = if (!disabled) Color.Transparent else Gray23),
    contentPadding = PaddingValues(0.dp),
    modifier = modifier
      .height(56.dp)
  ) {
    if (!disabled)
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .background(Brush.horizontalGradient(colors = Gradient06))
          .padding(horizontal = 16.dp)
          .fillMaxSize()
      ) {
        if (isLoading)
          CircularProgressIndicator(color = Color.White)
        else
          Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
          )
      }
    else
      Text(
        text = text,
        fontSize = 16.sp,
        color = Gray18,
        modifier = Modifier.padding(horizontal = 16.dp)
      )
  }
}

@Preview
@Composable
private fun PrimaryButtonPreview() {
  PrimaryButton(onClick = {}, text = "Enter")
}

@Preview
@Composable
private fun PrimaryButtonLoadingPreview() {
  PrimaryButton(onClick = {}, text = "Enter", isLoading = true)
}