package com.example.proyectofinal.data.model

data class LoginResponse(
    val token: String,
    val name: String,
    val email: String
)