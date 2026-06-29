package com.example.proyectofinal.data.model

data class MatchResponse(
    val id: Int,
    val home_team: String,
    val away_team: String,
    val match_date: String,
    val phase: String,
    val group_name: String? = null,
    val status: String,
    val home_score: Int? = null,
    val away_score: Int? = null,
    val stadium: StadiumResponse? = null
)

data class StadiumResponse(
    val id: Int,
    val name: String,
    val city: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val capacity: Int
)