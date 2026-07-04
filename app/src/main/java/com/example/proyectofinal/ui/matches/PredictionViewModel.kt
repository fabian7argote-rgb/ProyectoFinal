package com.example.proyectofinal.ui.matches

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.data.datastore.UserPreferences
import com.example.proyectofinal.data.repository.AuthRepository
import com.example.proyectofinal.data.local.AppDatabase
import com.example.proyectofinal.data.local.entities.MatchEntity
import com.example.proyectofinal.data.repository.DataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class PredictionState(
    val homeGoals: String = "",
    val awayGoals: String = "",
    val matchInfo: MatchEntity? = null,
    val isLoading: Boolean = false,
    val message: String = "",
    val errorMessage: String = ""
)

class PredictionViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val dataRepository = DataRepository(
        db.matchDao(),
        db.groupDao(),
        db.stadiumDao(),
        db.profileDao()
    )
    private val authRepository = AuthRepository()
    private val preferences = UserPreferences(application)

    private val _state = MutableStateFlow(PredictionState())
    val state: StateFlow<PredictionState> = _state

    fun onHomeGoalsChange(value: String) {
        _state.value = _state.value.copy(homeGoals = value)
    }

    fun onAwayGoalsChange(value: String) {
        _state.value = _state.value.copy(awayGoals = value)
    }

    fun loadMatchInfo(matchId: Int) {
        viewModelScope.launch {
            val match = db.matchDao().getMatchById(matchId)
            _state.value = _state.value.copy(
                matchInfo = match,
                homeGoals = match?.predictedHomeScore?.toString() ?: "",
                awayGoals = match?.predictedAwayScore?.toString() ?: ""
            )
        }
    }

    fun savePrediction(matchId: Int) {
        val home = _state.value.homeGoals.toIntOrNull()
        val away = _state.value.awayGoals.toIntOrNull()

        if (home == null || away == null) {
            _state.value = _state.value.copy(
                errorMessage = "Ingresa goles válidos"
            )
            return
        }

        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(
                    isLoading = true,
                    errorMessage = "",
                    message = ""
                )

                val token = preferences.token.first()

                if (token.isNullOrEmpty()) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "No hay sesión activa"
                    )
                    return@launch
                }

                val response = authRepository.createPrediction(
                    token = token,
                    matchId = matchId,
                    home = home,
                    away = away
                )

                if (response.isSuccessful) {
                    // Actualizar localmente
                    _state.value.matchInfo?.let {
                        db.matchDao().insertMatches(listOf(
                            it.copy(
                                predictedHomeScore = home,
                                predictedAwayScore = away
                            )
                        ))
                    }

                    _state.value = _state.value.copy(
                        isLoading = false,
                        message = "Pronóstico guardado correctamente"
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "No se pudo guardar el pronóstico en el servidor"
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