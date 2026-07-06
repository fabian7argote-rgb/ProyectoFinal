package com.example.proyectofinal.ui.groups

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.data.datastore.UserPreferences
import com.example.proyectofinal.data.repository.AuthRepository
import com.example.proyectofinal.data.local.AppDatabase
import com.example.proyectofinal.data.repository.DataRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import android.util.Log
import org.json.JSONObject

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

    private val db = AppDatabase.getDatabase(application)
    private val dataRepository = DataRepository(
        db.matchDao(),
        db.groupDao(),
        db.stadiumDao(),
        db.profileDao()
    )
    private val authRepository = AuthRepository()
    private val preferences = UserPreferences(application)

    private val _state = MutableStateFlow(GroupsState())
    val state: StateFlow<GroupsState> = _state

    init {
        viewModelScope.launch {
            dataRepository.allGroups.collect { entities ->
                val groups = entities.map {
                    GroupUi(
                        id = it.id,
                        name = it.name,
                        participants = it.participantsCount,
                        score = it.userScore,
                        inviteCode = it.inviteCode
                    )
                }
                _state.value = _state.value.copy(groups = groups)
            }
        }
    }

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

                dataRepository.syncGroups(token)
                _state.value = _state.value.copy(isLoading = false)

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

                val response = authRepository.createGroup(token, name)

                if (response.isSuccessful) {
                    _state.value = _state.value.copy(newGroupName = "")
                    dataRepository.syncGroups(token)
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

        if (code.isEmpty()) {
            _state.value = _state.value.copy(
                errorMessage = "Ingresa el código de invitación"
            )
            return
        }

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

                val response = authRepository.joinGroup(
                    token = token,
                    inviteCode = code
                )

                if (response.isSuccessful) {

                    _state.value = _state.value.copy(
                        inviteCode = "",
                        isLoading = false,
                        errorMessage = ""
                    )

                    // Si utilizas Room:
                    dataRepository.syncGroups(token)

                    // Si NO utilizas Room, usa loadGroups() en vez de la línea anterior.
                    // loadGroups()

                } else {
                    val rawError = response.errorBody()
                        ?.string()
                        .orEmpty()

                    Log.e(
                        "JOIN_GROUP",
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
                        403 -> "No tienes permiso para unirte a este grupo."
                        404 -> "No existe un grupo con ese código."
                        409 -> "Ya perteneces a este grupo."
                        422 -> "El código de invitación no es válido."
                        else -> "No se pudo unir al grupo. Error ${response.code()}."
                    }

                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = message
                    )
                }

            } catch (e: Exception) {
                Log.e(
                    "JOIN_GROUP",
                    "Error al unirse",
                    e
                )

                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = e.localizedMessage
                        ?: "Error de conexión con el servidor"
                )
            }
        }
    }
}
