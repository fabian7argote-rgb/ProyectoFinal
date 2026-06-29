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

data class StadiumUi(
    val id: Int,
    val name: String,
    val city: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val capacity: Int
)

data class StadiumState(
    val stadiums: List<StadiumUi> = emptyList(),
    val filteredStadiums: List<StadiumUi> = emptyList(),
    val search: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

class StadiumsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository()
    private val preferences = UserPreferences(application)

    private val _state = MutableStateFlow(StadiumState())
    val state: StateFlow<StadiumState> = _state

    fun loadStadiums() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true, errorMessage = "")

                val token = preferences.token.first()

                if (token.isNullOrEmpty()) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "No hay sesión activa"
                    )
                    return@launch
                }

                val response = repository.getStadiums(token)

                if (response.isSuccessful && response.body() != null) {
                    val list = response.body()!!.map {
                        StadiumUi(
                            id = it.id,
                            name = it.name,
                            city = it.city,
                            country = it.country,
                            latitude = it.latitude,
                            longitude = it.longitude,
                            capacity = it.capacity
                        )
                    }

                    _state.value = _state.value.copy(
                        stadiums = list,
                        filteredStadiums = list,
                        isLoading = false
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "No se pudieron cargar los estadios"
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

    fun onSearchChange(value: String) {
        val filtered = _state.value.stadiums.filter {
            it.name.contains(value, ignoreCase = true) ||
                    it.city.contains(value, ignoreCase = true) ||
                    it.country.contains(value, ignoreCase = true)
        }

        _state.value = _state.value.copy(
            search = value,
            filteredStadiums = filtered
        )
    }
}