package com.example.proyectofinal.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.data.datastore.UserPreferences
import com.example.proyectofinal.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
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

    private val repository = AuthRepository()
    private val preferences = UserPreferences(application)

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

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

                val response = repository.getProfile(token)

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!

                    _state.value = _state.value.copy(
                        name = body.name,
                        email = body.email,
                        totalScore = body.total_score ?: 0,
                        groups = body.groups_count ?: 0,
                        predictions = body.predictions_count ?: 0,
                        isLoading = false,
                        errorMessage = ""
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "No se pudo cargar el perfil"
                    )
                }

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
                    repository.logout(token)
                }

                preferences.clearSession()
                onLogout()

            } catch (e: Exception) {
                preferences.clearSession()
                onLogout()
            }
        }
    }
}