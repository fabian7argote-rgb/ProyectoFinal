package com.example.proyectofinal.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_session")

class UserPreferences(private val context: Context) {

    companion object {
        private val TOKEN = stringPreferencesKey("token")
        private val NAME = stringPreferencesKey("name")
        private val EMAIL = stringPreferencesKey("email")
    }

    val token: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[TOKEN]
    }

    val name: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[NAME]
    }

    val email: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[EMAIL]
    }

    suspend fun saveSession(
        token: String,
        name: String,
        email: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN] = token
            preferences[NAME] = name
            preferences[EMAIL] = email
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}