package com.example.proyectofinal.data.model

data class Match(
    val id: Int,
    val homeTeam: String,
    val awayTeam: String,
    val date: String,
    val stadium: String,
    val phase: String,
    val status: String,
    val result: String? = null,
    val prediction: String? = null
)