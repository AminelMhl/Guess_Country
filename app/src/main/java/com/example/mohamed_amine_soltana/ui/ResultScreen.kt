package com.example.mohamed_amine_soltana.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mohamed_amine_soltana.viewmodel.GameViewModel

@Composable
fun ResultScreen(isCorrect: Boolean, viewModel: GameViewModel, navController: NavController) {
    val index by viewModel.currentQuestionIndex.collectAsState()

    val previousIndex = (index - 1).coerceIn(0, viewModel.countries.size - 1)
    val country = viewModel.countries[previousIndex]

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isCorrect) {
            Text("Correct!", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
            Text("Country: ${country.name}")
            Text("Capital: ${country.capital}")
            Image(
                painter = painterResource(id = country.placeResId),
                contentDescription = "Feature",
                modifier = Modifier.size(200.dp)
            )
            Text("Feature: ${country.feature}")
        } else {
            Text("Wrong answer!", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
            Text("The correct answer was: ${country.name}")
        }

        Button(onClick = {
            if (viewModel.isGameOver()) {
                navController.navigate("score") {
                    popUpTo("question") { inclusive = true }
                }
            } else {
                navController.navigate("question")
            }
        }) {
            Text("Next")
        }
    }
}

