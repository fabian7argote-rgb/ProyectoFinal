package com.example.proyectofinal.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    val viewModel: RegisterViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.registerSuccess) {
        if (state.registerSuccess) {
            onRegisterSuccess()
        }
    }

    Box(
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
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF0E2A21)
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    text = "🏆 Crear cuenta",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = "Regístrate y empieza tu quiniela",
                    color = Color(0xFFCFEFE2)
                )

                OutlinedTextField(
                    value = state.name,
                    onValueChange = viewModel::onNameChange,
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = customTextFieldColors()
                )

                OutlinedTextField(
                    value = state.email,
                    onValueChange = viewModel::onEmailChange,
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = customTextFieldColors()
                )

                OutlinedTextField(
                    value = state.password,
                    onValueChange = viewModel::onPasswordChange,
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = customTextFieldColors()
                )

                if (state.errorMessage.isNotEmpty()) {
                    Text(
                        text = state.errorMessage,
                        color = Color(0xFFFF6B6B)
                    )
                }

                Button(
                    onClick = { viewModel.register() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFD166),
                        contentColor = Color(0xFF061A14)
                    )
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = Color(0xFF061A14)
                        )
                    } else {
                        Text("Registrarme")
                    }
                }

                TextButton(
                    onClick = onBackToLogin,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Ya tengo cuenta",
                        color = Color(0xFFFFD166)
                    )
                }
            }
        }
    }
}

@Composable
private fun customTextFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedBorderColor = Color(0xFFFFD166),
        unfocusedBorderColor = Color(0xFF6FAF98),
        focusedLabelColor = Color(0xFFFFD166),
        unfocusedLabelColor = Color(0xFFCFEFE2),
        cursorColor = Color(0xFFFFD166)
    )