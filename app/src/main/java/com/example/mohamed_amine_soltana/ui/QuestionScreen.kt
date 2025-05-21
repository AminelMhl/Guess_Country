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
    val country = viewModel.countries[index]
    var userInput by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("Question ${index + 1}", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        Image(
            painter = painterResource(id = country.flagResId),
            contentDescription = "Country Feature",
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
                    val isCorrect = country.name.equals(userInput.trim(), ignoreCase = true)
                    navController.navigate("result/$isCorrect")
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                val isCorrect = country.name.equals(userInput.trim(), ignoreCase = true)
                navController.navigate("result/$isCorrect")
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Text("Submit")
        }
    }
}