package com.example.proyectofinal.data.model

data class GroupDetailResponse(
    val id: Int = 0,
    val name: String = "",
    val invite_code: String? = null,
    val participants: List<GroupParticipantResponse>? = null,
    val next_matches: List<GroupNextMatchResponse>? = null
)

data class GroupParticipantResponse(
    val id: Int = 0,
    val name: String = "",
    val score: Int = 0
)

data class GroupNextMatchResponse(
    val id: Int = 0,
    val home_team: String = "",
    val away_team: String = "",
    val match_date: String = "",
    val phase: String = "",
    val status: String = ""
)

data class LeaderboardResponse(
    val position: Int = 0,
    val id: Int = 0,
    val name: String = "",
    val score: Int = 0
)