package com.example.proyectofinal.ui.matches

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.data.datastore.UserPreferences
import com.example.proyectofinal.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class MatchDetailState(
    val id: Int = 0,
    val homeTeam: String = "",
    val awayTeam: String = "",
    val date: String = "",
    val stadium: String = "",
    val phase: String = "",
    val status: String = "",
    val result: String = "Pendiente",
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

class MatchDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository()
    private val preferences = UserPreferences(application)

    private val _state = MutableStateFlow(MatchDetailState())
    val state: StateFlow<MatchDetailState> = _state

    fun loadMatch(matchId: Int) {
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

                val response = repository.getMatchById(token, matchId)

                if (response.isSuccessful && response.body() != null) {
                    val match = response.body()!!

                    val finalResult =
                        if (match.home_score != null && match.away_score != null) {
                            "${match.home_score} - ${match.away_score}"
                        } else {
                            "Pendiente"
                        }

                    _state.value = _state.value.copy(
                        id = match.id,
                        homeTeam = match.home_team,
                        awayTeam = match.away_team,
                        date = match.match_date,
                        stadium = match.stadium?.name ?: "Sin estadio",
                        phase = match.phase,
                        status = match.status,
                        result = finalResult,
                        isLoading = false
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "No se pudo cargar el partido"
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