package com.example.proyectofinal.ui.stadiums

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.data.datastore.UserPreferences
import com.example.proyectofinal.data.repository.AuthRepository
import com.example.proyectofinal.data.local.AppDatabase
import com.example.proyectofinal.data.repository.DataRepository
import kotlinx.coroutines.flow.*
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

    private val db = AppDatabase.getDatabase(application)
    private val dataRepository = DataRepository(
        db.matchDao(),
        db.groupDao(),
        db.stadiumDao(),
        db.profileDao()
    )
    private val preferences = UserPreferences(application)

    private val _state = MutableStateFlow(StadiumState())
    val state: StateFlow<StadiumState> = _state

    init {
        viewModelScope.launch {
            dataRepository.allStadiums.collect { entities ->
                val list = entities.map {
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
                    filteredStadiums = if (_state.value.search.isEmpty()) list else _state.value.filteredStadiums
                )
            }
        }
    }

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

                dataRepository.syncStadiums(token)
                _state.value = _state.value.copy(isLoading = false)

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