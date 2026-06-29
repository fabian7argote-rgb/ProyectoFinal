package com.example.proyectofinal.ui.matches

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MyPredictionsScreen() {

    val viewModel: MyPredictionsViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPredictions()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            Text(
                text = "Mis Pronósticos",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        if (state.isLoading) {
            item {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if (state.errorMessage.isNotEmpty()) {
            item {
                Text(
                    text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        if (state.predictions.isEmpty() && !state.isLoading) {
            item {
                Text("Todavía no realizaste pronósticos.")
            }
        }

        items(state.predictions) { prediction ->
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "${prediction.homeTeam} vs ${prediction.awayTeam}",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text("Fecha: ${prediction.date}")
                    Text("Mi pronóstico: ${prediction.predictedScore}")
                    Text("Resultado oficial: ${prediction.officialScore}")
                    Text("Puntos obtenidos: ${prediction.points}")
                    Text("Estado: ${prediction.status}")
                }
            }
        }
    }
}