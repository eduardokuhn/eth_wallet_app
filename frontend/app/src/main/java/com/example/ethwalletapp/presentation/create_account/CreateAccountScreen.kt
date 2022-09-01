import com.example.ethwalletapp.ui.create_account.CreateAccountStep
import com.example.ethwalletapp.ui.create_account.CreateAccountViewModel
import GradientButton
import TextInput
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.ethwalletapp.shared.navigation.Screen
import com.example.ethwalletapp.shared.theme.*

@Composable
fun CreateAccountScreen(
  navController: NavController?,
  viewModel: CreateAccountViewModel = CreateAccountViewModel()
) {
  val uiState = viewModel.uiState

  Scaffold(
    backgroundColor = Gray24
  ) { padding ->
    Column(
      modifier = Modifier
        .padding(padding)
        .fillMaxSize()
    ) {
      Spacer(modifier = Modifier.height(44.dp))
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = { navController?.navigate(Screen.StartScreen.route) },
        ) {
          Icon(
            Icons.Filled.ArrowBack,
            contentDescription = "Back to start screen",
            tint = Color.White
          )
        }
        StepsProgressBar(
          currentStep = uiState.value.currentStep,
          modifier = Modifier.fillMaxWidth()
        )
      }
      Spacer(modifier = Modifier.height(40.dp))
      Column(
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = uiState.value.currentStep.title(),
          fontSize = 16.sp,
          modifier = Modifier
            .graphicsLayer(alpha = 0.99f)
            .drawWithCache {
              val brush = Brush.horizontalGradient(Gradient07)
              onDrawWithContent {
                drawContent()
                drawRect(brush, blendMode = BlendMode.SrcAtop)
              }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = uiState.value.currentStep.description(),
          fontSize = 14.sp,
          color = Gray9
        )
      }
      Spacer(modifier = Modifier.height(40.dp))
      Column {
        TextInput(
          value = uiState.value.password,
          onValueChange = { viewModel.setPassword(it) },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
          visualTransformation =
            if (uiState.value.showPassword) VisualTransformation.None
            else PasswordVisualTransformation(),
          label = "Password",
          trailingIcon = {
            IconButton(
              onClick = { viewModel.toggleShowPassword() },
            ) {
              Icon(
                if (uiState.value.showPassword) Icons.Outlined.VisibilityOff
                else Icons.Outlined.Visibility,
                contentDescription =
                  if (uiState.value.showPassword) "Hide password"
                  else "Show password",
                tint = Color.White
              )
            }
          },
          helperText = "Must be at least 8 characters"
        )
        Spacer(modifier = Modifier.height(24.dp))
        TextInput(
          value = uiState.value.passwordConfirmation,
          onValueChange = { viewModel.setPasswordConfirmation(it) },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
          visualTransformation =
            if (uiState.value.showPasswordConfirmation) VisualTransformation.None
            else PasswordVisualTransformation(),
          label = "Password Confirmation",
          trailingIcon = {
            IconButton(
              onClick = { viewModel.toggleShowPasswordConfirmation() },
            ) {
              Icon(
                if (uiState.value.showPasswordConfirmation) Icons.Outlined.VisibilityOff
                else Icons.Outlined.Visibility,
                contentDescription =
                  if (uiState.value.showPasswordConfirmation) "Hide password"
                  else "Show password",
                tint = Color.White
              )
            }
          },
          helperText = "Must be at least 8 characters"
        )
      }
      Spacer(modifier = Modifier.height(40.dp))
      Row(
        verticalAlignment = Alignment.Top
      ) {
        Checkbox(
          checked = uiState.value.isChecked,
          onCheckedChange = { viewModel.toggleIsChecked() },
          colors = CheckboxDefaults.colors(
            checkedColor = Primary5,
            uncheckedColor = Gray22
          )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "I understand that anybody can recover this password for me.",
          fontSize = 14.sp,
          color = Color.White
        )
      }
      Spacer(modifier = Modifier.weight(1f))
      GradientButton(
        onClick = { /*TODO*/ },
        text = "Create Wallet"
      )
      Spacer(modifier = Modifier.height(42.dp))
    }
  }
}

@Preview
@Composable
fun CreateAccountScreenPreview() {
  CreateAccountScreen(null)
}

@Composable
fun StepsProgressBar(
  currentStep: CreateAccountStep,
  pathEffect: ((from: Int) -> PathEffect?)? = null,
  modifier: Modifier = Modifier,
) {
  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
  ) {
    Canvas(modifier = Modifier
      .fillMaxWidth()
      .zIndex(-1f)
    ) {
      val height = drawContext.size.height
      val width = drawContext.size.width

      val yOffset = height / 2
      val stepWidth = width / 3

      var startOffset = stepWidth / 2
      var endOffset = startOffset

      repeat(3 - 1) {
        endOffset += stepWidth
        drawLine(
          start = Offset(startOffset, yOffset),
          end = Offset(endOffset, yOffset),
          color = if (3 - 1 <= currentStep.index()) Primary5 else Gray21,
          strokeWidth = 1.dp.toPx(),
          pathEffect = pathEffect?.invoke(it)
        )
      }
    }
    Row(
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.fillMaxWidth()
    ) {
      repeat(3) { index ->
        Box(contentAlignment = Alignment.Center) {
          StepDot(index = index, currentStep = currentStep)
        }
      }
    }
  }
}

@Composable
fun StepDot(index: Int, currentStep: CreateAccountStep) {
  val color = if (index <= currentStep.index()) Primary5 else Gray21

  Canvas(
    modifier = Modifier
      .size(8.dp),
    onDraw = {
      drawCircle(color = color)
    }
  )
}