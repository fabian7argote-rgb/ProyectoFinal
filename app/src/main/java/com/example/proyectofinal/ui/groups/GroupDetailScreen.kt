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
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@Composable
fun GroupDetailScreen(
    groupId: Int,
    onMatchClick: (Int) -> Unit
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
            items(
                items = state.nextMatches,
                key = { it.id }
            ) { match ->
                GroupMatchCard(
                    match = match,
                    onClick = {
                        onMatchClick(match.id)
                    }
                )
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
    match: GroupMatchUi,
    onClick: () -> Unit
) {
    val canPredict = match.status.lowercase() in listOf(
        "scheduled",
        "pending",
        "programmed"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        enabled = canPredict,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF102C44),
            disabledContainerColor = Color(0xFF263845)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Text(
                text = "${match.homeTeam} vs ${match.awayTeam}",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Fecha: ${formatMatchDate(match.date)}",
                color = Color(0xFFCFEFE2)
            )

            Text(
                text = "Fase: ${translatePhase(match.phase)}",
                color = Color(0xFFCFEFE2)
            )

            Text(
                text = "Estado: ${translateStatus(match.status)}",
                color = statusColor(match.status)
            )

            HorizontalDivider(
                color = Color(0xFF426579)
            )

            Text(
                text = if (canPredict) {
                    "Toca este partido para hacer tu pronóstico"
                } else {
                    "Este partido ya no acepta pronósticos"
                },
                color = if (canPredict) {
                    Color(0xFFFFD166)
                } else {
                    Color(0xFFB0BEC5)
                },
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
private fun translateStatus(status: String): String {
    return when (status.lowercase()) {
        "scheduled" -> "Programado"
        "pending" -> "Pendiente"
        "in_progress" -> "En juego"
        "live" -> "En vivo"
        "finished" -> "Finalizado"
        "completed" -> "Finalizado"
        "postponed" -> "Aplazado"
        "cancelled" -> "Cancelado"
        "canceled" -> "Cancelado"
        else -> status.replace("_", " ")
            .replaceFirstChar { it.uppercase() }
    }
}

private fun translatePhase(phase: String): String {
    return when (phase.lowercase()) {
        "group" -> "Fase de grupos"
        "group_stage" -> "Fase de grupos"
        "round_of_32" -> "Dieciseisavos de final"
        "round_of_16" -> "Octavos de final"
        "quarter_finals" -> "Cuartos de final"
        "quarterfinals" -> "Cuartos de final"
        "semi_finals" -> "Semifinales"
        "semifinals" -> "Semifinales"
        "third_place" -> "Tercer puesto"
        "final" -> "Final"
        else -> phase.replace("_", " ")
            .replaceFirstChar { it.uppercase() }
    }
}

private fun statusColor(status: String): Color {
    return when (status.lowercase()) {
        "scheduled", "pending" -> Color(0xFFFFD166)
        "in_progress", "live" -> Color(0xFF4CAF50)
        "finished", "completed" -> Color(0xFF90CAF9)
        "cancelled", "canceled" -> Color(0xFFFF6B6B)
        "postponed" -> Color(0xFFFFA726)
        else -> Color.White
    }
}
private fun formatMatchDate(rawDate: String): String {
    return try {
        /*
         * La API devuelve 6 dígitos en los milisegundos:
         * 2026-07-06T19:00:00.000000Z
         *
         * SimpleDateFormat trabaja mejor con 3:
         * 2026-07-06T19:00:00.000Z
         */
        val normalizedDate = rawDate.replace(
            Regex("""\.(\d{3})\d*Z$"""),
            ".$1Z"
        )

        val inputFormat = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.US
        ).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

        val outputFormat = SimpleDateFormat(
            "dd/MM/yyyy - HH:mm",
            Locale("es", "ES")
        ).apply {
            timeZone = TimeZone.getDefault()
        }

        val parsedDate = inputFormat.parse(normalizedDate)

        if (parsedDate != null) {
            outputFormat.format(parsedDate)
        } else {
            rawDate
        }

    } catch (e: Exception) {
        rawDate
    }
}