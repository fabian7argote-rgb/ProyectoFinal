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

data class MyPredictionsState(
    val predictions: List<MyPredictionUi> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

data class MyPredictionUi(
    val id: Int,
    val matchId: Int,
    val homeTeam: String,
    val awayTeam: String,
    val predictedScore: String,
    val officialScore: String,
    val points: Int,
    val status: String,
    val date: String
)

class MyPredictionsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository()
    private val preferences = UserPreferences(application)

    private val _state = MutableStateFlow(MyPredictionsState())
    val state: StateFlow<MyPredictionsState> = _state

    fun loadPredictions() {
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

                val response = repository.getMyPredictions(token)

                if (response.isSuccessful && response.body() != null) {
                    val list = response.body()!!.map {
                        MyPredictionUi(
                            id = it.id,
                            matchId = it.match_id,
                            homeTeam = it.match.home_team,
                            awayTeam = it.match.away_team,
                            predictedScore = "${it.home_score} - ${it.away_score}",
                            officialScore = if (
                                it.match.home_score != null &&
                                it.match.away_score != null
                            ) {
                                "${it.match.home_score} - ${it.match.away_score}"
                            } else {
                                "Pendiente"
                            },
                            points = it.points_earned,
                            status = it.status,
                            date = it.match.match_date
                        )
                    }

                    _state.value = _state.value.copy(
                        predictions = list,
                        isLoading = false
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "No se pudieron cargar los pronósticos"
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