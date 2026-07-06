package com.example.proyectofinal.data.model

import com.google.gson.annotations.SerializedName

data class JoinGroupRequest(
    @SerializedName("invite_code")
    val inviteCode: String
)