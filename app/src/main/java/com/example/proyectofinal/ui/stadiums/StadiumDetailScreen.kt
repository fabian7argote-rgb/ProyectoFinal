package com.example.proyectofinal.ui.stadiums

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun StadiumDetailScreen(
    stadiumId: Int
) {
    val viewModel: StadiumDetailViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(stadiumId) {
        viewModel.loadStadiumDetail(stadiumId)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            Text(
                text = if (state.name.isNotEmpty()) state.name else "Detalle del Estadio",
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

        item {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Ciudad: ${state.city}")
                    Text("País: ${state.country}")
                    Text("Capacidad: ${state.capacity}")
                    Text("Latitud: ${state.latitude}")
                    Text("Longitud: ${state.longitude}")
                }
            }
        }

        item {
            Text(
                text = "Partidos en este estadio",
                style = MaterialTheme.typography.titleLarge
            )
        }

        if (state.matches.isEmpty() && !state.isLoading) {
            item {
                Text("No hay partidos registrados para este estadio.")
            }
        } else {
            items(state.matches) { match ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("${match.homeTeam} vs ${match.awayTeam}")
                        Text("Fecha: ${match.date}")
                        Text("Fase: ${match.phase}")
                        Text("Estado: ${match.status}")
                    }
                }
            }
        }
    }
}