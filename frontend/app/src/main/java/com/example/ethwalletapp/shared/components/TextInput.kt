import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ethwalletapp.shared.theme.Gray12
import com.example.ethwalletapp.shared.theme.Gray22

@Composable
fun TextInput(
  value: String,
  onValueChange: (String) -> Unit,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  visualTransformation: VisualTransformation = VisualTransformation.None,
  label: String,
  trailingIcon: @Composable () -> Unit,
  helperText: String? = null,
) {


  Column {
    BasicTextField(
      value = value,
      onValueChange = onValueChange,
      singleLine = true,
      keyboardOptions = keyboardOptions,
      visualTransformation =  visualTransformation,
      decorationBox = { innerTextField ->
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            // TODO fix padding right
            .padding(horizontal = 16.dp)
            .fillMaxSize()
        ) {
          Column(
            modifier = Modifier
              .weight(1f)
          ) {
            Text(
              text = label,
              fontSize = 12.sp,
              color = Gray12
            )
            innerTextField()
          }
          Column {
            trailingIcon.invoke()
          }
        }
      },
      modifier = Modifier
        .height(64.dp)
        .fillMaxWidth()
        .border(
          width = 1.dp,
          color = Gray22,
          shape = RoundedCornerShape(16.dp)
        )
        .background(
          color = Color.Transparent,
          shape = RoundedCornerShape(16.dp)
        )
    )
    if (helperText != null) {
      Spacer(modifier = Modifier.height(4.dp))
      Row(
        modifier = Modifier
          .padding(horizontal = 16.dp)
      ) {
        Text(
          text = helperText,
          fontSize = 12.sp,
          color = Gray12
        )
      }
    }
  }
}

@Preview
@Composable
fun TextInputPreview() {
  TextInput(
    value = "",
    onValueChange = {},
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    visualTransformation = VisualTransformation.None,
    label = "New Password",
    trailingIcon = {
      IconButton(
        onClick = { },
      ) {
        Icon(
          Icons.Outlined.Visibility,
          contentDescription = "Show password",
          tint = Color.White
        )
      }
    },
    helperText = "Must be at least 8 characters"
  )
}