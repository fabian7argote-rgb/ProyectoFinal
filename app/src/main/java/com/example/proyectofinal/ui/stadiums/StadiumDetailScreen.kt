package com.example.proyectofinal.ui.stadiums

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
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun StadiumDetailScreen(
    stadiumId: Int
) {
    val viewModel: StadiumDetailViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(stadiumId) {
        viewModel.loadStadiumDetail(stadiumId)
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(
                state.latitude,
                state.longitude
            ),
            14f
        )
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
                text = if (state.name.isNotEmpty()) state.name else "Detalle del Estadio",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
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
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF0E2A21)
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "🏟 Información de la sede",
                        color = Color(0xFFFFD166),
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = "Ciudad: ${state.city}",
                        color = Color.White
                    )

                    Text(
                        text = "País: ${state.country}",
                        color = Color.White
                    )

                    Text(
                        text = "Capacidad: ${state.capacity}",
                        color = Color.White
                    )

                    Divider(color = Color(0xFF6FAF98))

                    Text(
                        text = "Latitud: ${state.latitude}",
                        color = Color(0xFFCFEFE2)
                    )

                    Text(
                        text = "Longitud: ${state.longitude}",
                        color = Color(0xFFCFEFE2)
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
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
                        text = "🗺 Ubicación del estadio",
                        color = Color(0xFFFFD166),
                        style = MaterialTheme.typography.titleLarge
                    )

                    GoogleMap(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp),
                        cameraPositionState = cameraPositionState
                    ) {
                        Marker(
                            state = MarkerState(
                                position = LatLng(
                                    state.latitude,
                                    state.longitude
                                )
                            ),
                            title = state.name,
                            snippet = "${state.city}, ${state.country}"
                        )
                    }

                    Text(
                        text = "Ciudad: ${state.city}",
                        color = Color.White
                    )

                    Text(
                        text = "País: ${state.country}",
                        color = Color.White
                    )
                }
            }
        }

        item {
            Text(
                text = "⚽ Partidos en este estadio",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
        }

        if (state.matches.isEmpty() && !state.isLoading) {
            item {
                Text(
                    text = "No hay partidos registrados para este estadio.",
                    color = Color(0xFFCFEFE2)
                )
            }
        } else {
            items(state.matches) { match ->
                StadiumMatchCard(match)
            }
        }
    }
}

@Composable
fun StadiumMatchCard(
    match: StadiumMatchUi
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
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