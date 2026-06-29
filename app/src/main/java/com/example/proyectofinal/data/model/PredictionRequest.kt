package com.example.proyectofinal.data.model

data class PredictionRequest(
    val match_id: Int,
    val home_score: Int,
    val away_score: Int
)