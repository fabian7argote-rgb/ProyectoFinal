package com.example.proyectofinal.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.data.datastore.UserPreferences
import com.example.proyectofinal.data.repository.AuthRepository
import com.example.proyectofinal.data.local.AppDatabase
import com.example.proyectofinal.data.repository.DataRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ProfileState(
    val name: String = "Usuario Demo",
    val email: String = "admin@gmail.com",
    val totalScore: Int = 120,
    val groups: Int = 3,
    val predictions: Int = 15,
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val dataRepository = DataRepository(
        db.matchDao(),
        db.groupDao(),
        db.stadiumDao(),
        db.profileDao()
    )
    private val authRepository = AuthRepository()
    private val preferences = UserPreferences(application)

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    init {
        viewModelScope.launch {
            dataRepository.userProfile.collect { entity ->
                entity?.let {
                    _state.value = _state.value.copy(
                        name = it.name,
                        email = it.email,
                        totalScore = it.totalScore,
                        groups = it.groupsCount,
                        predictions = it.predictionsCount
                    )
                }
            }
        }
    }

    fun loadProfile() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)

                val token = preferences.token.first()

                if (token.isNullOrEmpty()) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "No hay sesión activa"
                    )
                    return@launch
                }

                dataRepository.syncProfile(token)
                _state.value = _state.value.copy(isLoading = false)

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Error de conexión"
                )
            }
        }
    }

    fun logout(onLogout: () -> Unit) {
        viewModelScope.launch {
            try {
                val token = preferences.token.first()

                if (!token.isNullOrEmpty()) {
                    authRepository.logout(token)
                }

                dataRepository.clearLocalData()
                preferences.clearSession()
                onLogout()

            } catch (e: Exception) {
                dataRepository.clearLocalData()
                preferences.clearSession()
                onLogout()
            }
        }
    }
}
