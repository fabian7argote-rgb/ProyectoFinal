package com.example.proyectofinal.ui.matches

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.data.datastore.UserPreferences
import com.example.proyectofinal.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject

data class PredictionState(
    val homeGoals: String = "",
    val awayGoals: String = "",
    val isLoading: Boolean = false,
    val message: String = "",
    val errorMessage: String = "",

    // Este valor funciona como evento de navegación.
    val predictionSaved: Boolean = false
)

class PredictionViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = AuthRepository()
    private val preferences = UserPreferences(application)

    private val _state = MutableStateFlow(PredictionState())
    val state: StateFlow<PredictionState> = _state

    fun onHomeGoalsChange(value: String) {
        /*
         * Solo permitimos números.
         * Esto evita que el usuario escriba letras.
         */
        if (value.all { it.isDigit() }) {
            _state.value = _state.value.copy(
                homeGoals = value,
                errorMessage = ""
            )
        }
    }

    fun onAwayGoalsChange(value: String) {
        if (value.all { it.isDigit() }) {
            _state.value = _state.value.copy(
                awayGoals = value,
                errorMessage = ""
            )
        }
    }

    fun savePrediction(matchId: Int) {

        val home = _state.value.homeGoals.toIntOrNull()
        val away = _state.value.awayGoals.toIntOrNull()

        if (matchId <= 0) {
            _state.value = _state.value.copy(
                errorMessage = "El identificador del partido no es válido"
            )
            return
        }

        if (home == null || away == null) {
            _state.value = _state.value.copy(
                errorMessage = "Ingresa los goles de ambos equipos"
            )
            return
        }

        if (home < 0 || away < 0) {
            _state.value = _state.value.copy(
                errorMessage = "Los goles no pueden ser negativos"
            )
            return
        }

        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(
                    isLoading = true,
                    message = "",
                    errorMessage = "",
                    predictionSaved = false
                )

                val token = preferences.token.first()

                if (token.isNullOrBlank()) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "No hay una sesión activa"
                    )
                    return@launch
                }

                val response = repository.createPrediction(
                    token = token,
                    matchId = matchId,
                    home = home,
                    away = away
                )

                if (response.isSuccessful) {

                    Log.d(
                        "CREATE_PREDICTION",
                        "Pronóstico guardado. HTTP ${response.code()}"
                    )

                    _state.value = _state.value.copy(
                        homeGoals = "",
                        awayGoals = "",
                        isLoading = false,
                        message = "Pronóstico guardado correctamente",
                        errorMessage = "",
                        predictionSaved = true
                    )

                } else {

                    val rawError = response.errorBody()
                        ?.string()
                        .orEmpty()

                    Log.e(
                        "CREATE_PREDICTION",
                        "HTTP ${response.code()} - $rawError"
                    )

                    val backendMessage = try {
                        JSONObject(rawError)
                            .optString("message")
                            .takeIf { it.isNotBlank() }
                    } catch (e: Exception) {
                        null
                    }

                    val message = backendMessage ?: when (response.code()) {
                        401 -> "Tu sesión expiró. Inicia sesión nuevamente."
                        403 -> "No tienes permiso para pronosticar este partido."
                        404 -> "El partido no existe."
                        409 -> "Ya registraste un pronóstico para este partido."
                        422 -> "El pronóstico contiene datos no válidos."
                        else -> "No se pudo guardar el pronóstico. Error ${response.code()}."
                    }

                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = message,
                        predictionSaved = false
                    )
                }

            } catch (e: Exception) {

                Log.e(
                    "CREATE_PREDICTION",
                    "Error guardando el pronóstico",
                    e
                )

                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = e.localizedMessage
                        ?: "Error de conexión con el servidor",
                    predictionSaved = false
                )
            }
        }
    }

    /**
     * Se ejecuta después de navegar.
     * Evita que Compose intente navegar varias veces
     * durante una recomposición.
     */
    fun consumePredictionSaved() {
        _state.value = _state.value.copy(
            predictionSaved = false
        )
    }
}