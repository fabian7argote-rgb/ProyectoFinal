package com.example.proyectofinal.data.model

data class ProfileResponse(
    val name: String,
    val email: String,
    val total_score: Int? = 0,
    val groups_count: Int? = 0,
    val predictions_count: Int? = 0
)