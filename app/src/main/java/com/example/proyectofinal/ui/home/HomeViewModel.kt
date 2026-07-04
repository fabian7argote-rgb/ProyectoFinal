package com.example.proyectofinal.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.data.local.AppDatabase
import com.example.proyectofinal.data.repository.DataRepository
import com.example.proyectofinal.data.datastore.UserPreferences
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeState(
    val name: String = "Cargando...",
    val email: String = "",
    val isLoading: Boolean = false
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = DataRepository(
        db.matchDao(),
        db.groupDao(),
        db.stadiumDao(),
        db.profileDao()
    )
    private val preferences = UserPreferences(application)

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    init {
        viewModelScope.launch {
            repository.userProfile.collect { entity ->
                entity?.let {
                    _state.value = _state.value.copy(
                        name = it.name,
                        email = it.email
                    )
                }
            }
        }
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            preferences.token.first()?.let { token ->
                repository.syncProfile(token)
                repository.syncGroups(token)
                repository.syncMatches(token)
            }
        }
    }
}
