package com.example.proyectofinal.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matches")
data class MatchEntity(
    @PrimaryKey val id: Int,
    val homeTeam: String,
    val awayTeam: String,
    val date: String,
    val stadium: String,
    val phase: String,
    val status: String,
    val homeScore: Int? = null,
    val awayScore: Int? = null,
    val predictedHomeScore: Int? = null,
    val predictedAwayScore: Int? = null
)
