import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ethwalletapp.utils.Routes

@Composable
fun NavigatorHost(
  modifier: Modifier = Modifier,
  navController: NavHostController = rememberNavController(),
  startDestination: String = Routes.START_SCREEN
) {
  NavHost(
    modifier = modifier,
    navController = navController,
    startDestination = startDestination,
  ) {
    composable(Routes.START_SCREEN) {
      StartScreen()
    }
  }
}