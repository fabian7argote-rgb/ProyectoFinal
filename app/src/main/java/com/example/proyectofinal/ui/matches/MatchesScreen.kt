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

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll

@Composable
fun MatchesScreen(
    onMatchClick: (Int) -> Unit = {}
) {
    val viewModel: MatchesViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadMatches()
    }

    Scaffold(
        topBar = {
            Column {
                Text(
                    text = "Partidos",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 8.dp),
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
                        selected = state.selectedFilter == "Fase grupos",
                        onClick = { viewModel.filterByStatus("Fase grupos") },
                        label = { Text("Fase grupos") }
                    )
                }
                
                if (state.isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    ) { padding ->
        if (state.filteredMatches.isEmpty() && !state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("No hay partidos para mostrar")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(state.filteredMatches) { match ->
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
    }
}