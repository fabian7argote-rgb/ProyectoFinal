package com.example.proyectofinal.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen() {

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
                        text = "120 pts",
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
                    value = "3",
                    modifier = Modifier.weight(1f)
                )

                HomeMiniCard(
                    title = "Pronósticos",
                    value = "15",
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

                    Text(
                        text = "Argentina vs Brasil",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = "15 Jun 2026 - 18:00",
                        color = Color(0xFFD6F5E8)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFD166),
                            contentColor = Color(0xFF061A14)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Hacer pronóstico")
                    }
                }
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
            QuickActionCard("👥 Mis grupos", "Crea o únete a una quiniela")
        }

        item {
            QuickActionCard("⚽ Partidos", "Consulta calendario y resultados")
        }

        item {
            QuickActionCard("🏟 Sedes", "Explora los estadios del Mundial")
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
    subtitle: String
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0E2A21)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                color = Color(0xFFCFEFE2)
            )
        }
    }
}