package com.example.proyectofinal.ui.stadiums

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun StadiumsScreen(
    onStadiumClick: (Int) -> Unit = {}
) {
    val viewModel: StadiumsViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    val context = LocalContext.current

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasLocationPermission = granted
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(39.8283, -98.5795),
            3.5f
        )
    }

    LaunchedEffect(Unit) {
        viewModel.loadStadiums()

        if (!hasLocationPermission) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    LaunchedEffect(state.filteredStadiums) {
        val first = state.filteredStadiums.firstOrNull()
        if (first != null) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    LatLng(first.latitude, first.longitude),
                    4f
                )
            )
        }
    }

    Column(
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
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Sedes del Mundial",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Explora estadios, ciudades y capacidad de cada sede.",
            color = Color(0xFFCFEFE2)
        )

        OutlinedTextField(
            value = state.search,
            onValueChange = viewModel::onSearchChange,
            label = { Text("Buscar estadio, ciudad o país") },
            modifier = Modifier.fillMaxWidth(),
            colors = stadiumTextFieldColors()
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF0E2A21)
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "🗺️ Mapa interactivo",
                    color = Color(0xFFFFD166),
                    style = MaterialTheme.typography.titleLarge
                )

                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                    cameraPositionState = cameraPositionState
                ) {
                    state.filteredStadiums.forEach { stadium ->
                        Marker(
                            state = MarkerState(
                                position = LatLng(
                                    stadium.latitude,
                                    stadium.longitude
                                )
                            ),
                            title = stadium.name,
                            snippet = "${stadium.city}, ${stadium.country}"
                        )
                    }
                }

                if (!hasLocationPermission) {
                    Text(
                        text = "Permite la ubicación para centrar el mapa en tu posición actual.",
                        color = Color.White
                    )

                    Button(
                        onClick = {
                            permissionLauncher.launch(
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFD166),
                            contentColor = Color(0xFF061A14)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Permitir ubicación")
                    }
                } else {
                    Text(
                        text = "Ubicación permitida.",
                        color = Color(0xFFCFEFE2)
                    )
                }
            }
        }

        if (state.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFFFD166)
            )
        }

        if (state.errorMessage.isNotEmpty()) {
            Text(
                text = state.errorMessage,
                color = Color(0xFFFF6B6B)
            )
        }

        Text(
            text = "Lista de sedes",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (state.filteredStadiums.isEmpty() && !state.isLoading) {
                item {
                    Text(
                        text = "No se encontraron sedes.",
                        color = Color(0xFFCFEFE2)
                    )
                }
            } else {
                items(state.filteredStadiums) { stadium ->
                    StadiumCard(
                        stadium = stadium,
                        onClick = { onStadiumClick(stadium.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun StadiumCard(
    stadium: StadiumUi,
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "🏟 ${stadium.name}",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "${stadium.city}, ${stadium.country}",
                color = Color(0xFFCFEFE2)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StadiumInfoBox(
                    title = "Capacidad",
                    value = stadium.capacity.toString()
                )

                StadiumInfoBox(
                    title = "Latitud",
                    value = stadium.latitude.toString()
                )
            }

            Text(
                text = "Toca para ver partidos en este estadio",
                color = Color(0xFFFFD166)
            )
        }
    }
}

@Composable
fun StadiumInfoBox(
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
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
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
private fun stadiumTextFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedBorderColor = Color(0xFFFFD166),
        unfocusedBorderColor = Color(0xFF6FAF98),
        focusedLabelColor = Color(0xFFFFD166),
        unfocusedLabelColor = Color(0xFFCFEFE2),
        cursorColor = Color(0xFFFFD166)
    )