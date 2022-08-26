package com.example.ethwalletapp.screens

import android.widget.Space
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ethwalletapp.ui.theme.Gray24

@Composable
fun CreateAccountScreen() {
  Scaffold(
    backgroundColor = Gray24
  ) { padding ->
    Column(
      modifier = Modifier
        .padding(padding)
        .fillMaxSize()
    ) {
      Spacer(modifier = Modifier.height(44.dp))
      Row {
        IconButton(
          onClick = { /*TODO*/ },
        ) {
          Icon(
            Icons.Filled.ArrowBack,
            contentDescription = "Back to Start Screen",
            tint = Color.White
          )
        }
        Spacer(modifier = Modifier.width(48.dp))
        StepsProgressBar()
      }
    }
  }
}

@Preview
@Composable
fun CreateAccountScreenPreview() {
  CreateAccountScreen()
}

@Composable
fun StepsProgressBar(
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier.fillMaxWidth()
  ) {
    for (step in CreateAccountSteps.values()) {
      Step()
    }
  }
}

@Preview
@Composable
fun StepsProgressBarPreview() {
  StepsProgressBar()
}

@Composable
fun Step() {
  Box {
    Canvas(
      modifier = Modifier
        .size(8.dp)
        .align(Alignment.CenterEnd)
        .border(
          shape = CircleShape,
          width = 2.dp,
          color = Color.Blue
        ),
      onDraw = {
        drawCircle(color = Color.Green)
      }
    )
    Divider(
      color = Color.Blue,
      thickness = 2.dp,
      modifier = Modifier.align(Alignment.CenterStart)
    )
  }
}