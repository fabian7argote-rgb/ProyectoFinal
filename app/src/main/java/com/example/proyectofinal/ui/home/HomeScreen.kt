package com.example.proyectofinal.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@Composable
fun HomeScreen(
    onNavigateToGroups: () -> Unit,
    onNavigateToMatches: () -> Unit,
    onNavigateToStadiums: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNextMatchClick: (Int) -> Unit
) {

    val viewModel: HomeViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadHomeData()
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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            Text(
                text = "Quiniela Mundial 2026",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            Text(
                text = "Bienvenido de nuevo 👋",
                color = Color(0xFFD6F5E8),
                style = MaterialTheme.typography.titleMedium
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF0F5B42)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Tu puntaje actual",
                        color = Color(0xFFCFEFE2)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${state.totalScore} pts",
                        color = Color(0xFFFFD166),
                        style = MaterialTheme.typography.headlineLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Sigue pronosticando para subir en el ranking.",
                        color = Color.White
                    )
                }
            }
        }

        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                HomeMiniCard(
                    title = "Grupos",
                    value = state.groupsCount.toString(),
                    modifier = Modifier.weight(1f)
                )

                HomeMiniCard(
                    title = "Pronósticos",
                    value = state.predictionsCount.toString(),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF102C44)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = "Próximo partido",
                        color = Color(0xFFFFD166),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    if (state.nextMatchId != null) {

                        Text(
                            text = "${state.nextMatchHomeTeam} vs ${state.nextMatchAwayTeam}",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = formatHomeMatchDate(state.nextMatchDate),
                            color = Color(0xFFD6F5E8)
                        )

                    } else {

                        Text(
                            text = "No hay próximos partidos",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = "Consulta nuevamente más tarde",
                            color = Color(0xFFD6F5E8)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            state.nextMatchId?.let { matchId ->
                                onNextMatchClick(matchId)
                            }
                        },
                        enabled = state.nextMatchId != null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFD166),
                            contentColor = Color(0xFF061A14),
                            disabledContainerColor = Color(0xFF6F745F),
                            disabledContentColor = Color.LightGray
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (state.nextMatchId != null) {
                                "Hacer pronóstico"
                            } else {
                                "Sin partidos disponibles"
                            }
                        )

                    }   }
            }
        }

        item {
            Text(
                text = "Accesos rápidos",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
        }

        item {
            QuickActionCard(
                title = "👥 Mis grupos",
                subtitle = "Crea o únete a una quiniela",
                onClick = onNavigateToGroups
            )
        }

        item {
            QuickActionCard(
                title = "⚽ Partidos",
                subtitle = "Consulta calendario y resultados",
                onClick = onNavigateToMatches
            )
        }

        item {
            QuickActionCard(
                title = "🏟 Sedes",
                subtitle = "Explora los estadios del Mundial",
                onClick = onNavigateToStadiums
            )
        }

        item {
            QuickActionCard(
                title = "👤 Mi perfil",
                subtitle = "Consulta tu puntaje y tus estadísticas",
                onClick = onNavigateToProfile
            )
        }
    }
}

@Composable
fun HomeMiniCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF123B59)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                color = Color(0xFFCFEFE2)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                color = Color(0xFFFFD166),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0E2A21)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = subtitle,
                    color = Color(0xFFCFEFE2)
                )
            }

            Text(
                text = "›",
                color = Color(0xFFFFD166),
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
private fun formatHomeMatchDate(rawDate: String): String {
    if (rawDate.isBlank()) {
        return "Fecha no disponible"
    }

    return try {
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
            Locale("es", "BO")
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