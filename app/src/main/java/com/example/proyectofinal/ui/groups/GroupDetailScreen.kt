package com.example.proyectofinal.ui.groups

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

        item {
            Text(
                text = if (state.groupName.isNotEmpty()) state.groupName else "Detalle del Grupo",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            Text(
                text = "Ranking, integrantes y próximos partidos de esta quiniela.",
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

        item {
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
                        text = "Código de invitación",
                        color = Color(0xFFCFEFE2)
                    )

                    Text(
                        text = state.inviteCode,
                        color = Color(0xFFFFD166),
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = "Participantes: ${state.participants.size}",
                        color = Color.White
                    )
                }
            }
        }

        item {
            Text(
                text = "🏆 Clasificación",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
        }

        if (state.leaderboard.isEmpty() && !state.isLoading) {
            item {
                Text(
                    text = "No hay clasificación disponible.",
                    color = Color(0xFFCFEFE2)
                )
            }
        } else {
            items(state.leaderboard) { user ->
                LeaderboardCard(user)
            }
        }

        item {
            Text(
                text = "👥 Integrantes",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
        }

        if (state.participants.isEmpty() && !state.isLoading) {
            item {
                Text(
                    text = "No hay integrantes disponibles.",
                    color = Color(0xFFCFEFE2)
                )
            }
        } else {
            items(state.participants) { user ->
                ParticipantCard(user)
            }
        }

        item {
            Text(
                text = "⚽ Próximos partidos",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
        }

        if (state.nextMatches.isEmpty() && !state.isLoading) {
            item {
                Text(
                    text = "No hay próximos partidos.",
                    color = Color(0xFFCFEFE2)
                )
            }
        } else {
            items(state.nextMatches) { match ->
                GroupMatchCard(match)
            }
        }
    }
}

@Composable
fun LeaderboardCard(
    user: LeaderboardUi
) {
    val medal = when (user.position) {
        1 -> "🥇"
        2 -> "🥈"
        3 -> "🥉"
        else -> "#${user.position}"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (user.position == 1) {
                Color(0xFF0F5B42)
            } else {
                Color(0xFF102C44)
            }
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "$medal ${user.name}",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Posición ${user.position}",
                    color = Color(0xFFCFEFE2)
                )
            }

            Text(
                text = "${user.score} pts",
                color = Color(0xFFFFD166),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun ParticipantCard(
    user: GroupParticipantUi
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0E2A21)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = user.name,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "${user.score} pts",
                color = Color(0xFFFFD166)
            )
        }
    }
}

@Composable
fun GroupMatchCard(
    match: GroupMatchUi
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF102C44)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
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
                text = "Fase: ${match.phase}",
                color = Color(0xFFCFEFE2)
            )

            Text(
                text = "Estado: ${match.status}",
                color = Color(0xFFFFD166)
            )
        }
    }
}