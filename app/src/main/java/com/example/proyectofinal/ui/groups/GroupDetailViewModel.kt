package com.example.proyectofinal.ui.groups

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.data.datastore.UserPreferences
import com.example.proyectofinal.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class GroupDetailState(
    val groupName: String = "",
    val inviteCode: String = "",
    val participants: List<GroupParticipantUi> = emptyList(),
    val leaderboard: List<LeaderboardUi> = emptyList(),
    val nextMatches: List<GroupMatchUi> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

data class GroupParticipantUi(
    val id: Int,
    val name: String,
    val score: Int
)

data class LeaderboardUi(
    val position: Int,
    val id: Int,
    val name: String,
    val score: Int
)

data class GroupMatchUi(
    val id: Int,
    val homeTeam: String,
    val awayTeam: String,
    val date: String,
    val phase: String,
    val status: String
)

class GroupDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository()
    private val preferences = UserPreferences(application)

    private val _state = MutableStateFlow(GroupDetailState())
    val state: StateFlow<GroupDetailState> = _state

    fun loadGroupDetail(groupId: Int) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(
                    isLoading = true,
                    errorMessage = ""
                )

                val token = preferences.token.first()

                if (token.isNullOrEmpty()) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "No hay sesión activa"
                    )
                    return@launch
                }

                val detailResponse = repository.getGroupDetail(token, groupId)
                val leaderboardResponse = repository.getGroupLeaderboard(token, groupId)

                if (detailResponse.isSuccessful && detailResponse.body() != null) {
                    val detail = detailResponse.body()!!

                    val participants = detail.participants.map {
                        GroupParticipantUi(
                            id = it.id,
                            name = it.name,
                            score = it.score
                        )
                    }

                    val matches = detail.next_matches.map {
                        GroupMatchUi(
                            id = it.id,
                            homeTeam = it.home_team,
                            awayTeam = it.away_team,
                            date = it.match_date,
                            phase = it.phase,
                            status = it.status
                        )
                    }

                    val leaderboard = if (
                        leaderboardResponse.isSuccessful &&
                        leaderboardResponse.body() != null
                    ) {
                        leaderboardResponse.body()!!.map {
                            LeaderboardUi(
                                position = it.position,
                                id = it.id,
                                name = it.name,
                                score = it.score
                            )
                        }
                    } else {
                        emptyList()
                    }

                    _state.value = _state.value.copy(
                        groupName = detail.name,
                        inviteCode = detail.invite_code,
                        participants = participants,
                        leaderboard = leaderboard,
                        nextMatches = matches,
                        isLoading = false
                    )

                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "No se pudo cargar el detalle del grupo"
                    )
                }

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = e.localizedMessage ?: "Error de conexión"
                )
            }
        }
    }
}