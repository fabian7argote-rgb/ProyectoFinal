package com.example.proyectofinal.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    onMyPredictions: () -> Unit
) {
    val viewModel: ProfileViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Perfil",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium
        )

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

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF0E2A21)
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "👤 ${state.name}",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = state.email,
                    color = Color(0xFFCFEFE2)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileStatCard(
                title = "Puntaje",
                value = "${state.totalScore}",
                modifier = Modifier.weight(1f)
            )

            ProfileStatCard(
                title = "Grupos",
                value = "${state.groups}",
                modifier = Modifier.weight(1f)
            )
        }

        ProfileStatCard(
            title = "Pronósticos realizados",
            value = "${state.predictions}",
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onMyPredictions,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFD166),
                contentColor = Color(0xFF061A14)
            )
        ) {
            Text("Ver mis pronósticos")
        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = {
                viewModel.logout(onLogout)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFFFF6B6B)
            )
        ) {
            Text("Cerrar sesión")
        }
    }
}

@Composable
fun ProfileStatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF102C44)
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