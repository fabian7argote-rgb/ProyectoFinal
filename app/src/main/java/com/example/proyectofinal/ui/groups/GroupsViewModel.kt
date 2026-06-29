package com.example.proyectofinal.ui.groups

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.data.datastore.UserPreferences
import com.example.proyectofinal.data.repository.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class GroupsState(
    val groups: List<GroupUi> = emptyList(),
    val newGroupName: String = "",
    val inviteCode: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

data class GroupUi(
    val id: Int,
    val name: String,
    val participants: Int,
    val score: Int,
    val inviteCode: String
)

class GroupsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository()
    private val preferences = UserPreferences(application)

    private val _state = MutableStateFlow(GroupsState())
    val state: StateFlow<GroupsState> = _state

    fun onNewGroupNameChange(value: String) {
        _state.value = _state.value.copy(newGroupName = value)
    }

    fun onInviteCodeChange(value: String) {
        _state.value = _state.value.copy(inviteCode = value)
    }

    fun loadGroups() {
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

                val response = repository.getGroups(token)

                if (response.isSuccessful && response.body() != null) {
                    val groups = response.body()!!.map {
                        GroupUi(
                            id = it.id,
                            name = it.name,
                            participants = it.participants_count,
                            score = it.user_score,
                            inviteCode = it.invite_code
                        )
                    }

                    _state.value = _state.value.copy(
                        groups = groups,
                        isLoading = false
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "No se pudieron cargar los grupos"
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

    fun createGroup() {
        val name = _state.value.newGroupName.trim()
        if (name.isEmpty()) return

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

                val response = repository.createGroup(token, name)

                if (response.isSuccessful) {
                    _state.value = _state.value.copy(newGroupName = "")
                    loadGroups()
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "No se pudo crear el grupo"
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

    fun joinGroup() {
        val code = _state.value.inviteCode.trim()
        if (code.isEmpty()) return

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

                val response = repository.joinGroup(token, code)

                if (response.isSuccessful) {
                    _state.value = _state.value.copy(inviteCode = "")
                    loadGroups()
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "No se pudo unir al grupo"
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