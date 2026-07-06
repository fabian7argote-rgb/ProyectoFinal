package com.example.proyectofinal.ui.matches

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MatchDetailScreen(
    matchId: Int,
    onPredictionSaved: () -> Unit
){
    val matchViewModel: MatchDetailViewModel = viewModel()
    val matchState by matchViewModel.state.collectAsState()

    val predictionViewModel: PredictionViewModel = viewModel()
    val predictionState by predictionViewModel.state.collectAsState()

    LaunchedEffect(predictionState.predictionSaved) {
        if (predictionState.predictionSaved) {

            /*
             * Primero consumimos el evento para evitar
             * navegaciones repetidas.
             */
            predictionViewModel.consumePredictionSaved()

            /*
             * Después avisamos a AppNavigation que debe
             * regresar a la pantalla de grupos.
             */
            onPredictionSaved()
        }
    }

    LaunchedEffect(matchId) {
        matchViewModel.loadMatch(matchId)
    }

    Column(
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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Detalle del Partido",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium
        )

        if (matchState.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFFFD166)
            )
        }

        if (matchState.errorMessage.isNotEmpty()) {
            Text(
                text = matchState.errorMessage,
                color = Color(0xFFFF6B6B)
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF102C44)
            )
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${matchState.homeTeam} vs ${matchState.awayTeam}",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )

                Text("Fecha: ${matchState.date}", color = Color(0xFFCFEFE2))
                Text("Estadio: ${matchState.stadium}", color = Color(0xFFCFEFE2))
                Text("Fase: ${matchState.phase}", color = Color(0xFFCFEFE2))
                Text("Estado: ${matchState.status}", color = Color(0xFFFFD166))
                Text("Resultado: ${matchState.result}", color = Color(0xFFFFD166))
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF0E2A21)
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                Text(
                    text = "🎫 Boleto de Pronóstico",
                    color = Color(0xFFFFD166),
                    style = MaterialTheme.typography.titleLarge
                )

                OutlinedTextField(
                    value = predictionState.homeGoals,
                    onValueChange = predictionViewModel::onHomeGoalsChange,
                    label = { Text("Goles ${matchState.homeTeam.ifEmpty { "local" }}") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    colors = predictionTextFieldColors()
                )

                OutlinedTextField(
                    value = predictionState.awayGoals,
                    onValueChange = predictionViewModel::onAwayGoalsChange,
                    label = { Text("Goles ${matchState.awayTeam.ifEmpty { "visitante" }}") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    colors = predictionTextFieldColors()
                )

                if (predictionState.errorMessage.isNotEmpty()) {
                    Text(
                        text = predictionState.errorMessage,
                        color = Color(0xFFFF6B6B)
                    )
                }

                if (predictionState.message.isNotEmpty()) {
                    Text(
                        text = predictionState.message,
                        color = Color(0xFFFFD166)
                    )
                }

                Button(
                    onClick = {
                        predictionViewModel.savePrediction(matchId)
                    },
                    enabled = !predictionState.isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFD166),
                        contentColor = Color(0xFF061A14)
                    )
                ) {
                    if (predictionState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = Color(0xFF061A14)
                        )
                    } else {
                        Text("Confirmar pronóstico")
                    }
                }
            }
        }
    }
}

@Composable
private fun predictionTextFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedBorderColor = Color(0xFFFFD166),
        unfocusedBorderColor = Color(0xFF6FAF98),
        focusedLabelColor = Color(0xFFFFD166),
        unfocusedLabelColor = Color(0xFFCFEFE2),
        cursorColor = Color(0xFFFFD166)
    )