package com.example.ethwalletapp.shared.utils

sealed class ViewState {
  object Unknown: ViewState()
  object Success: ViewState()
  object Loading: ViewState()
  object Error: ViewState()
}