package com.example.proyectofinal.ui.groups


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun GroupsScreen(
    onGroupClick: (Int) -> Unit
){
    val viewModel: GroupsViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadGroups()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            Text(
                text = "Mis Grupos",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            Card {

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Crear nuevo grupo")

                    OutlinedTextField(
                        value = state.newGroupName,
                        onValueChange = viewModel::onNewGroupNameChange,
                        label = { Text("Nombre del grupo") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = { viewModel.createGroup() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Crear grupo")
                    }
                }
            }
        }

        item {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Unirse a grupo")

                    OutlinedTextField(
                        value = state.inviteCode,
                        onValueChange = viewModel::onInviteCodeChange,
                        label = { Text("Código de invitación") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = { viewModel.joinGroup() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Unirse")
                    }
                }
            }
        }

        items(state.groups) { group ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onGroupClick(group.id) }

            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = group.name,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text("Participantes: ${group.participants}")
                    Text("Mi puntaje: ${group.score}")
                    Text("Código: ${group.inviteCode}")
                }
            }
        }
    }
}