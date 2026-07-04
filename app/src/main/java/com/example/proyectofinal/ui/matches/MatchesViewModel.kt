package com.example.proyectofinal.ui.matches

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.data.datastore.UserPreferences
import com.example.proyectofinal.data.repository.AuthRepository
import com.example.proyectofinal.data.local.AppDatabase
import com.example.proyectofinal.data.repository.DataRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.proyectofinal.data.api.ApiClient

data class MatchesState(
    val allMatches: List<MatchUi> = emptyList(),
    val filteredMatches: List<MatchUi> = emptyList(),
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

    private val db = AppDatabase.getDatabase(application)
    private val repository = DataRepository(
        db.matchDao(),
        db.groupDao(),
        db.stadiumDao(),
        db.profileDao()
    )
    private val preferences = UserPreferences(application)

    private val _state = MutableStateFlow(MatchesState())
    val state: StateFlow<MatchesState> = _state

    init {
        viewModelScope.launch {
            repository.allMatches.collect { entities ->
                val list = entities.map {
                    MatchUi(
                        id = it.id,
                        homeTeam = it.homeTeam,
                        awayTeam = it.awayTeam,
                        date = it.date,
                        stadium = it.stadium,
                        phase = it.phase,
                        status = it.status,
                        result = if (it.homeScore != null && it.awayScore != null) {
                            "${it.homeScore} - ${it.awayScore}"
                        } else null,
                        prediction = if (it.predictedHomeScore != null && it.predictedAwayScore != null) {
                            "${it.predictedHomeScore} - ${it.predictedAwayScore}"
                        } else null
                    )
                }
                _state.update { currentState ->
                    currentState.copy(
                        allMatches = list,
                        filteredMatches = applyFilter(list, currentState.selectedFilter)
                    )
                }
            }
        }
    }

    private fun applyFilter(matches: List<MatchUi>, filter: String): List<MatchUi> {
        return when (filter) {
            "Todos" -> matches
            "Pendiente" -> matches.filter { it.status == "scheduled" || it.status == "pending" }
            "Finalizado" -> matches.filter { it.status == "finished" }
            "Fase grupos" -> matches.filter { it.phase == "group" }
            else -> matches
        }
    }

    fun loadMatches() {
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

                repository.syncMatches(token)
                _state.value = _state.value.copy(isLoading = false)

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = e.localizedMessage ?: "Error de conexión"
                )
            }
        }
    }

    fun filterByStatus(status: String) {
        _state.update { currentState ->
            currentState.copy(
                selectedFilter = status,
                filteredMatches = applyFilter(currentState.allMatches, status)
            )
        }
    }
}