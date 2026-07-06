package com.example.proyectofinal.data.api


import com.example.proyectofinal.data.model.LoginRequest
import com.example.proyectofinal.data.model.RegisterRequest
import com.example.proyectofinal.data.model.LoginResponse
import com.example.proyectofinal.data.model.ProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Path
import com.example.proyectofinal.data.model.MatchResponse
import com.example.proyectofinal.data.model.MatchUpdatesResponse
import com.example.proyectofinal.data.model.CreateGroupRequest
import com.example.proyectofinal.data.model.CreatePredictionResponse
import com.example.proyectofinal.data.model.GroupResponse
import com.example.proyectofinal.data.model.JoinGroupRequest
import com.example.proyectofinal.data.model.StadiumResponse
import com.example.proyectofinal.data.model.GroupDetailResponse
import com.example.proyectofinal.data.model.LeaderboardResponse
import com.example.proyectofinal.data.model.MyPredictionResponse
import com.example.proyectofinal.data.model.PredictionRequest
import okhttp3.ResponseBody


interface ApiService {
    @POST("api/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @GET("api/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<ProfileResponse>
    @POST("api/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<LoginResponse>
    @GET("api/groups")
    suspend fun getGroups(
        @Header("Authorization") token: String
    ): Response<List<GroupResponse>>

    @POST("api/groups")
    suspend fun createGroup(
        @Header("Authorization") token: String,
        @Body request: CreateGroupRequest
    ): Response<GroupResponse>

    @POST("api/groups/join")
    suspend fun joinGroup(
        @Header("Authorization") token: String,
        @Body request: JoinGroupRequest
    ): Response<Unit>
    @POST("api/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<Map<String, String>>
    @GET("api/matches")
    suspend fun getMatches(
        @Header("Authorization") token: String,
        @Query("status") status: String? = null,
        @Query("phase") phase: String? = null,
        @Query("date") date: String? = null,
        @Query("next") next: Boolean? = null
    ): Response<List<MatchResponse>>

    @GET("api/matches/{id}")
    suspend fun getMatchById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<MatchResponse>

    @GET("api/matches/updates")
    suspend fun getMatchUpdates(
        @Header("Authorization") token: String,
        @Query("since") since: String? = null
    ): Response<MatchUpdatesResponse>
    @GET("api/stadiums")
    suspend fun getStadiums(
        @Header("Authorization") token: String
    ): Response<List<StadiumResponse>>

    @GET("api/stadiums/{id}")
    suspend fun getStadiumById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<StadiumResponse>

    @GET("api/stadiums/{id}/matches")
    suspend fun getStadiumMatches(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<List<MatchResponse>>
    @GET("api/groups/{id}")
    suspend fun getGroupDetail(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<GroupDetailResponse>

    @GET("api/groups/{id}/leaderboard")
    suspend fun getGroupLeaderboard(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<List<LeaderboardResponse>>
    @POST("api/predictions")
    suspend fun createPrediction(
        @Header("Authorization") token: String,
        @Body request: PredictionRequest
    ): Response<ResponseBody>

    @GET("api/predictions/me")
    suspend fun getMyPredictions(
        @Header("Authorization") token: String
    ): Response<List<MyPredictionResponse>>
}