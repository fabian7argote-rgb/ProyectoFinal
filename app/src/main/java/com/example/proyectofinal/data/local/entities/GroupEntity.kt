package com.example.proyectofinal.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_groups")
data class GroupEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val participantsCount: Int,
    val userScore: Int,
    val inviteCode: String
)
