package com.example.proyectofinal.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class ProfileEntity(
    @PrimaryKey val email: String,
    val name: String,
    val totalScore: Int,
    val groupsCount: Int,
    val predictionsCount: Int
)
