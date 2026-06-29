package com.example.proyectofinal.data.model

data class GroupDetailResponse(
    val id: Int,
    val name: String,
    val invite_code: String,
    val participants: List<GroupParticipantResponse>,
    val next_matches: List<GroupNextMatchResponse>
)

data class GroupParticipantResponse(
    val id: Int,
    val name: String,
    val score: Int
)

data class GroupNextMatchResponse(
    val id: Int,
    val home_team: String,
    val away_team: String,
    val match_date: String,
    val phase: String,
    val status: String
)

data class LeaderboardResponse(
    val position: Int,
    val id: Int,
    val name: String,
    val score: Int
)