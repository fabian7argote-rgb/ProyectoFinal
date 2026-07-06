package com.example.proyectofinal.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.data.datastore.UserPreferences
import com.example.proyectofinal.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class HomeState(
    val name: String = "",
    val email: String = "",
    val totalScore: Int = 0,
    val groupsCount: Int = 0,
    val predictionsCount: Int = 0,
    val nextMatchId: Int? = null,
    val nextMatchHomeTeam: String = "",
    val nextMatchAwayTeam: String = "",
    val nextMatchDate: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

class HomeViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = AuthRepository()
    private val preferences = UserPreferences(application)

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    fun loadHomeData() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(
                    isLoading = true,
                    errorMessage = ""
                )

                val token = preferences.token.first()

                if (token.isNullOrBlank()) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "No hay sesión activa"
                    )
                    return@launch
                }

                /*
                 * Primero consulta el perfil.
                 * Desde el perfil obtiene puntaje,
                 * cantidad de grupos y pronósticos.
                 */
                val profileResponse = repository.getProfile(token)

                /*
                 * Luego consulta solamente los próximos partidos.
                 */
                val matchesResponse = repository.getMatches(
                    token = token,
                    next = true
                )

                val profile = profileResponse.body()

                /*
                 * firstOrNull() obtiene el primer partido
                 * de la lista o devuelve null si no hay ninguno.
                 */
                val nextMatch = if (matchesResponse.isSuccessful) {
                    matchesResponse.body()
                        .orEmpty()
                        .firstOrNull()
                } else {
                    null
                }

                if (profileResponse.isSuccessful && profile != null) {
                    _state.value = _state.value.copy(
                        name = profile.name,
                        email = profile.email,

                        totalScore = profile.total_score ?: 0,
                        groupsCount = profile.groups_count ?: 0,
                        predictionsCount = profile.predictions_count ?: 0,

                        nextMatchId = nextMatch?.id,
                        nextMatchHomeTeam = nextMatch?.home_team.orEmpty(),
                        nextMatchAwayTeam = nextMatch?.away_team.orEmpty(),
                        nextMatchDate = nextMatch?.match_date.orEmpty(),

                        isLoading = false,
                        errorMessage = ""
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "No se pudo cargar el inicio"
                    )
                }

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = e.localizedMessage
                        ?: "Error de conexión"
                )
            }
        }
    }
}