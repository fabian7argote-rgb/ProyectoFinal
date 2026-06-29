package com.example.proyectofinal.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.data.datastore.UserPreferences
import com.example.proyectofinal.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val loginSuccess: Boolean = false
)

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository()
    private val preferences = UserPreferences(application)

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    fun onEmailChange(value: String) {
        _state.value = _state.value.copy(email = value)
    }

    fun onPasswordChange(value: String) {
        _state.value = _state.value.copy(password = value)
    }

    fun login() {
        val email = _state.value.email.trim()
        val password = _state.value.password.trim()

        if (email.isEmpty() || password.isEmpty()) {
            _state.value = _state.value.copy(
                errorMessage = "Completa todos los campos"
            )
            return
        }

        if (!email.contains("@")) {
            _state.value = _state.value.copy(
                errorMessage = "Correo inválido"
            )
            return
        }

        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(
                    isLoading = true,
                    errorMessage = ""
                )

                val response = repository.login(email, password)

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!

                    preferences.saveSession(
                        token = body.token,
                        name = body.name,
                        email = body.email
                    )

                    _state.value = _state.value.copy(
                        isLoading = false,
                        loginSuccess = true
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "Correo o contraseña incorrectos"
                    )
                }

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = e.localizedMessage ?: "Error desconocido"
                )
            }
        }
    }
}