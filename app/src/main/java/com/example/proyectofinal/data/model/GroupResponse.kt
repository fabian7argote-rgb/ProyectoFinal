package com.example.proyectofinal.data.model

data class GroupResponse(
    val id: Int,
    val name: String,
    val participants_count: Int,
    val user_score: Int,
    val invite_code: String
)