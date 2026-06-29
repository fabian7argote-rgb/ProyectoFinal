package com.example.proyectofinal.ui.matches

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MatchesScreen(
    onMatchClick: (Int) -> Unit = {}
) {
    val viewModel: MatchesViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadMatches()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Partidos",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = state.selectedFilter == "Todos",
                    onClick = { viewModel.filterByStatus("Todos") },
                    label = { Text("Todos") }
                )

                FilterChip(
                    selected = state.selectedFilter == "Pendiente",
                    onClick = { viewModel.filterByStatus("Pendiente") },
                    label = { Text("Pendientes") }
                )

                FilterChip(
                    selected = state.selectedFilter == "Finalizado",
                    onClick = { viewModel.filterByStatus("Finalizado") },
                    label = { Text("Finalizados") }
                )
                FilterChip(
                    selected = state.selectedFilter == "Próximos",
                    onClick = { viewModel.showNextMatches() },
                    label = { Text("Próximos") }
                )

                FilterChip(
                    selected = state.selectedFilter == "group",
                    onClick = { viewModel.filterByPhase("group") },
                    label = { Text("Fase grupos") }
                )
                var dateFilter by remember { mutableStateOf("") }

                OutlinedTextField(
                    value = dateFilter,
                    onValueChange = { dateFilter = it },
                    label = { Text("Fecha: YYYY-MM-DD") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        if (dateFilter.isNotBlank()) {
                            viewModel.filterByDate(dateFilter)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Filtrar por fecha")
                }
            }
        }

        items(state.matches) { match ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onMatchClick(match.id) }
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "${match.homeTeam} vs ${match.awayTeam}",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text("Fecha: ${match.date}")
                    Text("Estadio: ${match.stadium}")
                    Text("Fase: ${match.phase}")
                    Text("Estado: ${match.status}")

                    match.result?.let {
                        Text("Resultado: $it")
                    }

                    match.prediction?.let {
                        Text("Mi pronóstico: $it")
                    }
                }
            }
        }

    }
}