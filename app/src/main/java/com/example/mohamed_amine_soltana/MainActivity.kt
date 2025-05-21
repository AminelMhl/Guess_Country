package com.example.mohamed_amine_soltana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mohamed_amine_soltana.ui.*
import com.example.mohamed_amine_soltana.ui.theme.Mohamed_Amine_SoltanaTheme
import com.example.mohamed_amine_soltana.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Mohamed_Amine_SoltanaTheme {
                val viewModel: GameViewModel = viewModel()
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(navController)
                    }
                    composable("question") {
                        QuestionScreen(viewModel, navController)
                    }
                    composable(
                        "result/{isCorrect}",
                        arguments = listOf(navArgument("isCorrect") { type = NavType.BoolType })
                    ) { backStackEntry ->
                        val isCorrect = backStackEntry.arguments?.getBoolean("isCorrect") ?: false
                        ResultScreen(isCorrect, viewModel, navController)
                    }
                    composable("score") {
                        ScoreScreen(viewModel, navController)
                    }
                }
            }
        }
    }
}