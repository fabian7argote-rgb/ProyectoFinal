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
import com.example.proyectofinal.data.api.ApiClient

data class MatchesState(
    val matches: List<MatchUi> = emptyList(),
    val selectedFilter: String = "Todos",
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

data class MatchUi(
    val id: Int,
    val homeTeam: String,
    val awayTeam: String,
    val date: String,
    val stadium: String,
    val phase: String,
    val status: String,
    val result: String? = null,
    val prediction: String? = null
)

class MatchesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository()
    private val preferences = UserPreferences(application)

    private val _state = MutableStateFlow(MatchesState())
    val state: StateFlow<MatchesState> = _state

    fun loadMatches(
        status: String? = null,
        phase: String? = null,
        date: String? = null,
        next: Boolean? = null
    ) {
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

                val response = ApiClient.api.getMatches(
                    token = "Bearer $token",
                    status = status,
                    phase = phase,
                    date = date,
                    next = next
                )

                if (response.isSuccessful && response.body() != null) {
                    val list = response.body()!!.map {
                        MatchUi(
                            id = it.id,
                            homeTeam = it.home_team,
                            awayTeam = it.away_team,
                            date = it.match_date,
                            stadium = it.stadium?.name ?: "Sin estadio",
                            phase = it.phase,
                            status = it.status,
                            result = if (it.home_score != null && it.away_score != null) {
                                "${it.home_score} - ${it.away_score}"
                            } else null
                        )
                    }

                    _state.value = _state.value.copy(
                        matches = list,
                        isLoading = false
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "No se pudieron cargar los partidos"
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

    fun showAll() {
        _state.value = _state.value.copy(selectedFilter = "Todos")
        loadMatches()
    }

    fun showNextMatches() {
        _state.value = _state.value.copy(selectedFilter = "Próximos")
        loadMatches(next = true)
    }

    fun filterByStatus(status: String) {
        val apiStatus = when (status) {
            "Pendiente" -> "scheduled"
            "Finalizado" -> "finished"
            else -> null
        }

        _state.value = _state.value.copy(selectedFilter = status)
        loadMatches(status = apiStatus)
    }

    fun filterByPhase(phase: String) {
        _state.value = _state.value.copy(selectedFilter = phase)
        loadMatches(phase = phase)
    }

    fun filterByDate(date: String) {
        _state.value = _state.value.copy(selectedFilter = date)
        loadMatches(date = date)
    }
}