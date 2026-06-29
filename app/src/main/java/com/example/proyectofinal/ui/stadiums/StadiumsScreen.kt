package com.example.proyectofinal.ui.stadiums

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun StadiumsScreen(
    onStadiumClick: (Int) -> Unit = {}
) {
    val viewModel: StadiumsViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadStadiums()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            Text(
                text = "Sedes del Mundial",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            OutlinedTextField(
                value = state.search,
                onValueChange = viewModel::onSearchChange,
                label = { Text("Buscar estadio, ciudad o país") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Mapa interactivo",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Aquí se mostrará Google Maps con los marcadores de los estadios.")
                }
            }
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

        items(state.filteredStadiums) { stadium ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onStadiumClick(stadium.id)
                }
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = stadium.name,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text("Ciudad: ${stadium.city}")
                    Text("País: ${stadium.country}")
                    Text("Capacidad: ${stadium.capacity}")
                    Text("Coordenadas: ${stadium.latitude}, ${stadium.longitude}")
                }
            }
        }
    }
}