package com.example.proyectofinal.data.repository

import com.example.proyectofinal.data.api.ApiClient
import com.example.proyectofinal.data.model.LoginRequest
import com.example.proyectofinal.data.model.RegisterRequest
import com.example.proyectofinal.data.model.CreateGroupRequest
import com.example.proyectofinal.data.model.JoinGroupRequest
import com.example.proyectofinal.data.model.PredictionRequest

class AuthRepository {

    suspend fun login(email: String, password: String) =
        ApiClient.api.login(
            LoginRequest(
                email = email,
                password = password
            )
        )

    suspend fun getProfile(token: String) =
        ApiClient.api.getProfile("Bearer $token")

    suspend fun register(
        name: String,
        email: String,
        password: String
    ) =
        ApiClient.api.register(
            RegisterRequest(
                name = name,
                email = email,
                password = password,
                password_confirmation = password
            )
        )

    suspend fun getGroups(token: String) =
        ApiClient.api.getGroups("Bearer $token")

    suspend fun createGroup(token: String, name: String) =
        ApiClient.api.createGroup(
            "Bearer $token",
            CreateGroupRequest(name)
        )

    suspend fun joinGroup(token: String, inviteCode: String) =
        ApiClient.api.joinGroup(
            "Bearer $token",
            JoinGroupRequest(inviteCode)
        )

    suspend fun getMatches(
        token: String,
        status: String? = null,
        phase: String? = null,
        date: String? = null,
        next: Boolean? = null
    ) =
        ApiClient.api.getMatches(
            token = "Bearer $token",
            status = status,
            phase = phase,
            date = date,
            next = next
        )

    suspend fun getMatchById(token: String, id: Int) =
        ApiClient.api.getMatchById(
            token = "Bearer $token",
            id = id
        )

    suspend fun getStadiums(token: String) =
        ApiClient.api.getStadiums("Bearer $token")

    suspend fun getStadiumById(token: String, id: Int) =
        ApiClient.api.getStadiumById(
            token = "Bearer $token",
            id = id
        )

    suspend fun getStadiumMatches(token: String, id: Int) =
        ApiClient.api.getStadiumMatches(
            token = "Bearer $token",
            id = id
        )

    suspend fun logout(token: String) =
        ApiClient.api.logout("Bearer $token")

    suspend fun getGroupDetail(token: String, id: Int) =
        ApiClient.api.getGroupDetail(
            token = "Bearer $token",
            id = id
        )

    suspend fun getGroupLeaderboard(token: String, id: Int) =
        ApiClient.api.getGroupLeaderboard(
            token = "Bearer $token",
            id = id
        )

    suspend fun createPrediction(
        token: String,
        matchId: Int,
        home: Int,
        away: Int
    ) =
        ApiClient.api.createPrediction(
            "Bearer $token",
            PredictionRequest(
                match_id = matchId,
                home_score = home,
                away_score = away
            )
        )

    suspend fun getMyPredictions(token: String) =
        ApiClient.api.getMyPredictions("Bearer $token")

}


