package com.example.proyectofinal.data.model

data class MatchUpdatesResponse(
    val synced_at: String,
    val matches: List<MatchResponse>
)
