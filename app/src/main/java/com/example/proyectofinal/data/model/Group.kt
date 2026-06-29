package com.example.proyectofinal.data.model

data class Group(
    val id: Int,
    val name: String,
    val participants: Int,
    val score: Int,
    val inviteCode: String
)