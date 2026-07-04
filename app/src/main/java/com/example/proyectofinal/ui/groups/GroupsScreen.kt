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

@Composable
fun GroupsScreen(
    onGroupClick: (Int) -> Unit
) {
    val viewModel: GroupsViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadGroups()
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
                text = "Mis Grupos",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            Text(
                text = "Crea ligas privadas o únete con un código de invitación.",
                color = Color(0xFFCFEFE2)
            )
        }

        item {
            GroupActionCard(
                title = "Crear nuevo grupo",
                subtitle = "Organiza tu propia quiniela privada",
                textValue = state.newGroupName,
                onTextChange = viewModel::onNewGroupNameChange,
                label = "Nombre del grupo",
                buttonText = "Crear grupo",
                onButtonClick = { viewModel.createGroup() }
            )
        }

        item {
            GroupActionCard(
                title = "Unirse a grupo",
                subtitle = "Ingresa el código que te compartieron",
                textValue = state.inviteCode,
                onTextChange = viewModel::onInviteCodeChange,
                label = "Código de invitación",
                buttonText = "Unirme",
                onButtonClick = { viewModel.joinGroup() }
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
            Text(
                text = "Ligas activas",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
        }

        if (state.groups.isEmpty() && !state.isLoading) {
            item {
                Text(
                    text = "Todavía no perteneces a ningún grupo.",
                    color = Color(0xFFCFEFE2)
                )
            }
        }

        items(state.groups) { group ->
            GroupBetCard(
                group = group,
                onClick = { onGroupClick(group.id) }
            )
        }
    }
}

@Composable
fun GroupActionCard(
    title: String,
    subtitle: String,
    textValue: String,
    onTextChange: (String) -> Unit,
    label: String,
    buttonText: String,
    onButtonClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0E2A21)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                color = Color(0xFFFFD166),
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = subtitle,
                color = Color(0xFFCFEFE2)
            )

            OutlinedTextField(
                value = textValue,
                onValueChange = onTextChange,
                label = { Text(label) },
                modifier = Modifier.fillMaxWidth(),
                colors = groupTextFieldColors()
            )

            Button(
                onClick = onButtonClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFD166),
                    contentColor = Color(0xFF061A14)
                )
            ) {
                Text(buttonText)
            }
        }
    }
}

@Composable
fun GroupBetCard(
    group: GroupUi,
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
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "🏆 ${group.name}",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                GroupStatBox(
                    title = "Participantes",
                    value = group.participants.toString()
                )

                GroupStatBox(
                    title = "Mi puntaje",
                    value = group.score.toString()
                )
            }

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF0F5B42)
                )
            ) {
                Text(
                    text = "Código: ${group.inviteCode}",
                    color = Color(0xFFFFD166),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                )
            }

            Text(
                text = "Toca para ver clasificación y próximos partidos",
                color = Color(0xFFCFEFE2)
            )
        }
    }
}

@Composable
fun GroupStatBox(
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
            modifier = Modifier.padding(
                horizontal = 14.dp,
                vertical = 10.dp
            )
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
private fun groupTextFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedBorderColor = Color(0xFFFFD166),
        unfocusedBorderColor = Color(0xFF6FAF98),
        focusedLabelColor = Color(0xFFFFD166),
        unfocusedLabelColor = Color(0xFFCFEFE2),
        cursorColor = Color(0xFFFFD166)
    )