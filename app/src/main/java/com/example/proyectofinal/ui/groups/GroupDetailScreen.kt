package com.example.proyectofinal.ui.groups

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun GroupDetailScreen(
    groupId: Int
) {
    val viewModel: GroupDetailViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(groupId) {
        viewModel.loadGroupDetail(groupId)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            Text(
                text = if (state.groupName.isNotEmpty()) state.groupName else "Detalle del Grupo",
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
                    Text("Código de invitación: ${state.inviteCode}")
                    Text("Participantes: ${state.participants.size}")
                }
            }
        }

        item {
            Text(
                text = "Clasificación",
                style = MaterialTheme.typography.titleLarge
            )
        }

        if (state.leaderboard.isEmpty()) {
            item {
                Text("No hay clasificación disponible.")
            }
        } else {
            items(state.leaderboard) { user ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("${user.position}. ${user.name}")
                        Text("Puntaje: ${user.score}")
                    }
                }
            }
        }

        item {
            Text(
                text = "Integrantes",
                style = MaterialTheme.typography.titleLarge
            )
        }

        if (state.participants.isEmpty()) {
            item {
                Text("No hay integrantes disponibles.")
            }
        } else {
            items(state.participants) { user ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(user.name)
                        Text("Puntaje: ${user.score}")
                    }
                }
            }
        }

        item {
            Text(
                text = "Próximos partidos",
                style = MaterialTheme.typography.titleLarge
            )
        }

        if (state.nextMatches.isEmpty()) {
            item {
                Text("No hay próximos partidos.")
            }
        } else {
            items(state.nextMatches) { match ->
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