package pl.wsei.pam.lab06

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pl.wsei.pam.lab06.viewmodel.TodoViewModel

@Composable
fun MainScreen(todoViewModel: TodoViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            ListScreen(navController = navController, viewModel = todoViewModel)
        }
        composable("form") {
            FormScreen(navController = navController, viewModel = todoViewModel)
        }
    }
}
