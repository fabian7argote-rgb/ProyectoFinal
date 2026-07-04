package com.example.proyectofinal.ui.matches

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF061A14),
                        Color(0xFF0B3D2E),
                        Color(0xFF102C44)
                    )
                )
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        item {
            Text(
                text = "Mis Pronósticos",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            Text(
                text = "Historial de marcadores registrados y puntos obtenidos.",
                color = Color(0xFFCFEFE2)
            )
        }

        if (state.isLoading) {
            item {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFFFD166)
                )
            }
        }

        if (state.errorMessage.isNotEmpty()) {
            item {
                Text(
                    text = state.errorMessage,
                    color = Color(0xFFFF6B6B)
                )
            }
        }

        if (state.predictions.isEmpty() && !state.isLoading) {
            item {
                Text(
                    text = "Todavía no realizaste pronósticos.",
                    color = Color(0xFFCFEFE2)
                )
            }
        }

        items(state.predictions) { prediction ->
            PredictionTicketCard(prediction)
        }
    }
}

@Composable
fun PredictionTicketCard(
    prediction: MyPredictionUi
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0E2A21)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Text(
                text = "🎫 Ticket #${prediction.id}",
                color = Color(0xFFFFD166),
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "${prediction.homeTeam} vs ${prediction.awayTeam}",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Fecha: ${prediction.date}",
                color = Color(0xFFCFEFE2)
            )

            Divider(color = Color(0xFF6FAF98))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                TicketInfoBox(
                    title = "Mi pronóstico",
                    value = prediction.predictedScore,
                    modifier = Modifier.weight(1f)
                )

                TicketInfoBox(
                    title = "Resultado",
                    value = prediction.officialScore,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                TicketInfoBox(
                    title = "Puntos",
                    value = "${prediction.points}",
                    modifier = Modifier.weight(1f)
                )

                TicketInfoBox(
                    title = "Estado",
                    value = prediction.status,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun TicketInfoBox(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF102C44)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = title,
                color = Color(0xFFCFEFE2),
                style = MaterialTheme.typography.labelSmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                color = Color(0xFFFFD166),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}