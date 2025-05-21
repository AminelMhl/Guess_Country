package com.example.mohamed_amine_soltana.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mohamed_amine_soltana.viewmodel.GameViewModel

@Composable
fun QuestionScreen(viewModel: GameViewModel, navController: NavController) {
    val index by viewModel.currentQuestionIndex.collectAsState()
    val score by viewModel.score.collectAsState()

    if (viewModel.isGameOver()) {
        LaunchedEffect(Unit) {
            navController.navigate("score") {
                popUpTo("question") { inclusive = true }
            }
        }
        return
    }

    val country = viewModel.countries[index]
    var userInput by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Score: $score/${index}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Text("Question ${index + 1}/10", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        Image(
            painter = painterResource(id = country.flagResId),
            contentDescription = "Country Flag",
            modifier = Modifier.size(200.dp)
        )

        Spacer(Modifier.height(24.dp))

        Text("What country is this?", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = userInput,
            onValueChange = { userInput = it },
            label = { Text("Enter country name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    submitAnswer(viewModel, userInput, navController)
                    userInput = ""
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                submitAnswer(viewModel, userInput, navController)
                userInput = ""
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Text("Submit")
        }
    }
}

private fun submitAnswer(viewModel: GameViewModel, userInput: String, navController: NavController) {
    val isCorrect = viewModel.checkAnswer(userInput.trim())

    val isLastQuestion = viewModel.currentQuestionIndex.value == 9  // 10th question (index 9)

    viewModel.nextQuestion()

    if (isLastQuestion) {
        navController.navigate("score") {
            popUpTo("question") { inclusive = true }
        }
    } else {
        navController.navigate("result/$isCorrect")
    }
}