package com.example.proyectofinal.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Stadium

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToGroups: () -> Unit = {},
    onNavigateToMatches: () -> Unit = {},
    onNavigateToStadiums: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    val viewModel: HomeViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Quiniela Mundial 2026")
                }
            )
        }

    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)

        ) {

            item {
                Card {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = "Bienvenido",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(state.name)
                        Text(state.email)

                    }

                }

            }

            item {
                Button(
                    onClick = { onNavigateToGroups() },
                    modifier = Modifier.fillMaxWidth()

                ) {

                    Icon(Icons.Default.Groups, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Mis Grupos")

                }

            }

            item {
                Button(
                    onClick = { onNavigateToMatches() },
                    modifier = Modifier.fillMaxWidth()

                ) {

                    Icon(Icons.Default.SportsSoccer, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Partidos")

                }

            }

            item {
                Button(
                    onClick = { onNavigateToStadiums() },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Icon(Icons.Default.Stadium, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Mapa de Sedes")

                }

            }

            item {
                Button(
                    onClick = { onNavigateToProfile() },
                    modifier = Modifier.fillMaxWidth()

                ) {
                    Icon(Icons.Default.Person, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Perfil")

                }

            }

        }

    }

}