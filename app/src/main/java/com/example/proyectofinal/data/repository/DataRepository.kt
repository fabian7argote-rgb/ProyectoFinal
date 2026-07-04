package com.example.proyectofinal.data.repository

import com.example.proyectofinal.data.api.ApiClient
import com.example.proyectofinal.data.local.dao.*
import com.example.proyectofinal.data.local.entities.*
import kotlinx.coroutines.flow.Flow

class DataRepository(
    private val matchDao: MatchDao,
    private val groupDao: GroupDao,
    private val stadiumDao: StadiumDao,
    private val profileDao: ProfileDao
) {

    // Matches
    val allMatches: Flow<List<MatchEntity>> = matchDao.getAllMatches()

    suspend fun syncMatches(token: String) {
        val response = ApiClient.api.getMatches("Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { remoteMatches ->
                val entities = remoteMatches.map {
                    MatchEntity(
                        id = it.id,
                        homeTeam = it.home_team,
                        awayTeam = it.away_team,
                        date = it.match_date,
                        stadium = it.stadium?.name ?: "TBD",
                        phase = it.phase,
                        status = it.status,
                        homeScore = it.home_score,
                        awayScore = it.away_score
                    )
                }
                matchDao.insertMatches(entities)
            }
        }
    }

    // Groups
    val allGroups: Flow<List<GroupEntity>> = groupDao.getAllGroups()

    suspend fun syncGroups(token: String) {
        val response = ApiClient.api.getGroups("Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { remoteGroups ->
                val entities = remoteGroups.map {
                    GroupEntity(
                        id = it.id,
                        name = it.name,
                        participantsCount = it.participants_count,
                        userScore = it.user_score,
                        inviteCode = it.invite_code
                    )
                }
                groupDao.insertGroups(entities)
            }
        }
    }

    // Stadiums
    val allStadiums: Flow<List<StadiumEntity>> = stadiumDao.getAllStadiums()

    suspend fun syncStadiums(token: String) {
        val response = ApiClient.api.getStadiums("Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let { remoteStadiums ->
                val entities = remoteStadiums.map {
                    StadiumEntity(
                        id = it.id,
                        name = it.name,
                        city = it.city,
                        country = it.country,
                        latitude = it.latitude,
                        longitude = it.longitude,
                        capacity = it.capacity
                    )
                }
                stadiumDao.insertStadiums(entities)
            }
        }
    }

    // Profile
    val userProfile: Flow<ProfileEntity?> = profileDao.getProfile()

    suspend fun syncProfile(token: String) {
        val response = ApiClient.api.getProfile("Bearer $token")
        if (response.isSuccessful) {
            response.body()?.let {
                val entity = ProfileEntity(
                    email = it.email,
                    name = it.name,
                    totalScore = it.total_score ?: 0,
                    groupsCount = it.groups_count ?: 0,
                    predictionsCount = it.predictions_count ?: 0
                )
                profileDao.insertProfile(entity)
            }
        }
    }

    suspend fun clearLocalData() {
        matchDao.deleteAllMatches()
        groupDao.deleteAllGroups()
        stadiumDao.deleteAllStadiums()
        profileDao.deleteProfile()
    }
}
