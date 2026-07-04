package com.example.proyectofinal.ui.matches

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MatchesScreen(
    onMatchClick: (Int) -> Unit = {}
) {
    val viewModel: MatchesViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    var dateFilter by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadMatches()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF061A14),
                        Color(0xFF0B3D2E)
                    )
                )
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        item {
            Text(
                text = "Partidos",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            Text(
                text = "Filtra partidos y registra tus pronósticos",
                color = Color(0xFFCFEFE2)
            )
        }

        item {
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BetFilterChip(
                    text = "Todos",
                    selected = state.selectedFilter == "Todos",
                    onClick = { viewModel.showAll() }
                )

                BetFilterChip(
                    text = "Próximos",
                    selected = state.selectedFilter == "Próximos",
                    onClick = { viewModel.showNextMatches() }
                )

                BetFilterChip(
                    text = "Pendientes",
                    selected = state.selectedFilter == "Pendiente",
                    onClick = { viewModel.filterByStatus("Pendiente") }
                )

                BetFilterChip(
                    text = "Finalizados",
                    selected = state.selectedFilter == "Finalizado",
                    onClick = { viewModel.filterByStatus("Finalizado") }
                )

                BetFilterChip(
                    text = "Fase grupos",
                    selected = state.selectedFilter == "group",
                    onClick = { viewModel.filterByPhase("group") }
                )
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF0E2A21)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = dateFilter,
                        onValueChange = { dateFilter = it },
                        label = { Text("Fecha: YYYY-MM-DD") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFFFFD166),
                            unfocusedBorderColor = Color(0xFF6FAF98),
                            focusedLabelColor = Color(0xFFFFD166),
                            unfocusedLabelColor = Color(0xFFCFEFE2),
                            cursorColor = Color(0xFFFFD166)
                        )
                    )

                    Button(
                        onClick = {
                            if (dateFilter.isNotBlank()) {
                                viewModel.filterByDate(dateFilter)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFD166),
                            contentColor = Color(0xFF061A14)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Filtrar por fecha")
                    }
                }
            }
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

        if (state.matches.isEmpty() && !state.isLoading) {
            item {
                Text(
                    text = "No hay partidos disponibles.",
                    color = Color(0xFFCFEFE2)
                )
            }
        }

        items(state.matches) { match ->
            MatchBetCard(
                match = match,
                onClick = { onMatchClick(match.id) }
            )
        }
    }
}

@Composable
fun MatchBetCard(
    match: MatchUi,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF102C44)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Text(
                text = match.phase.uppercase(),
                color = Color(0xFFFFD166),
                style = MaterialTheme.typography.labelLarge
            )

            Text(
                text = "${match.homeTeam} vs ${match.awayTeam}",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Fecha: ${match.date}",
                color = Color(0xFFCFEFE2)
            )

            Text(
                text = "Estadio: ${match.stadium}",
                color = Color(0xFFCFEFE2)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                BetOddBox(
                    title = "Local",
                    value = "1.85"
                )

                BetOddBox(
                    title = "Empate",
                    value = "3.20"
                )

                BetOddBox(
                    title = "Visitante",
                    value = "2.10"
                )
            }

            if (match.result != null) {
                Text(
                    text = "Resultado: ${match.result}",
                    color = Color(0xFFFFD166)
                )
            } else {
                Text(
                    text = "Toca para pronosticar",
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun BetOddBox(
    title: String,
    value: String
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0F5B42)
        )
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 14.dp,
                vertical = 10.dp
            )
        ) {
            Text(
                text = title,
                color = Color(0xFFCFEFE2),
                style = MaterialTheme.typography.labelSmall
            )

            Text(
                text = value,
                color = Color(0xFFFFD166),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun BetFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(text)
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color(0xFFFFD166),
            selectedLabelColor = Color(0xFF061A14),
            containerColor = Color(0xFF0E2A21),
            labelColor = Color.White
        )
    )
}