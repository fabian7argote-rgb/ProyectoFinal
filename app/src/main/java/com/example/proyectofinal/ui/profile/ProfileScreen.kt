package com.example.proyectofinal.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    onMyPredictions: () -> Unit
){
    val viewModel: ProfileViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Perfil",
            style = MaterialTheme.typography.headlineMedium
        )

        if (state.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (state.errorMessage.isNotEmpty()) {
            Text(
                text = state.errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }

        Card {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = state.name,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = state.email)
            }
        }

        Card {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Puntaje acumulado")

                Text(
                    text = "${state.totalScore} puntos",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }

        Card {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Cantidad de grupos")
                Text(text = "${state.groups}")
            }
        }

        Card {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Pronósticos realizados")
                Text(text = "${state.predictions}")
            }
        }

        Button(
            onClick = onMyPredictions,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mis Pronósticos")
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                viewModel.logout(onLogout)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }
    }
}