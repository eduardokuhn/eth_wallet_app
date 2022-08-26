import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

enum class CreateAccountSteps {
  CreatePassword,
  SecureWallet,
  ConfirmRecoveryPhrase,
}

class CreateAccountViewModel : ViewModel() {
  private var currentStep = MutableStateFlow(CreateAccountSteps.CreatePassword)
  get() = currentStep
  fun setCurrentStep(step: CreateAccountSteps) {
    currentStep.value = step
  }
}