package com.example.proyectofinal.ui.stadiums

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.data.datastore.UserPreferences
import com.example.proyectofinal.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class StadiumDetailState(
    val name: String = "",
    val city: String = "",
    val country: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val capacity: Int = 0,
    val matches: List<StadiumMatchUi> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

data class StadiumMatchUi(
    val id: Int,
    val homeTeam: String,
    val awayTeam: String,
    val date: String,
    val phase: String,
    val status: String
)

class StadiumDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository()
    private val preferences = UserPreferences(application)

    private val _state = MutableStateFlow(StadiumDetailState())
    val state: StateFlow<StadiumDetailState> = _state

    fun loadStadiumDetail(stadiumId: Int) {
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

                val stadiumResponse = repository.getStadiumById(token, stadiumId)
                val matchesResponse = repository.getStadiumMatches(token, stadiumId)

                if (stadiumResponse.isSuccessful && stadiumResponse.body() != null) {
                    val stadium = stadiumResponse.body()!!

                    val matches = if (
                        matchesResponse.isSuccessful &&
                        matchesResponse.body() != null
                    ) {
                        matchesResponse.body()!!.map {
                            StadiumMatchUi(
                                id = it.id,
                                homeTeam = it.home_team,
                                awayTeam = it.away_team,
                                date = it.match_date,
                                phase = it.phase,
                                status = it.status
                            )
                        }
                    } else {
                        emptyList()
                    }

                    _state.value = _state.value.copy(
                        name = stadium.name,
                        city = stadium.city,
                        country = stadium.country,
                        latitude = stadium.latitude,
                        longitude = stadium.longitude,
                        capacity = stadium.capacity,
                        matches = matches,
                        isLoading = false
                    )

                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "No se pudo cargar el estadio"
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