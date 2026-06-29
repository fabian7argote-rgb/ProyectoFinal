package com.example.proyectofinal.data.model

data class CreatePredictionResponse(
    val message: String,
    val prediction: PredictionSavedResponse
)

data class PredictionSavedResponse(
    val id: Int,
    val match_id: Int,
    val home_score: Int,
    val away_score: Int,
    val status: String
)

data class MyPredictionResponse(
    val id: Int,
    val match_id: Int,
    val home_score: Int,
    val away_score: Int,
    val points_earned: Int,
    val status: String,
    val match: PredictionMatchResponse
)

data class PredictionMatchResponse(
    val id: Int,
    val home_team: String,
    val away_team: String,
    val match_date: String,
    val status: String,
    val home_score: Int?,
    val away_score: Int?,
    val phase: String
)