package com.example.proyectofinal.ui.matches

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MatchDetailScreen(
    matchId: Int
) {

    val viewModel: PredictionViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Registrar Pronóstico",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = state.homeGoals,
            onValueChange = viewModel::onHomeGoalsChange,
            label = { Text("Goles Local") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.awayGoals,
            onValueChange = viewModel::onAwayGoalsChange,
            label = { Text("Goles Visitante") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (state.errorMessage.isNotEmpty()) {
            Text(
                state.errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (state.message.isNotEmpty()) {
            Text(
                state.message,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                viewModel.savePrediction(matchId)
            }
        ) {
            Text("Guardar Pronóstico")
        }

    }

}