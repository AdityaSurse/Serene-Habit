package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferences(private val context: Context) {
    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USER_ID = stringPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_PHOTO_URL = stringPreferencesKey("user_photo_url")
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }

    val userId: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_ID] ?: "guest"
    }

    val userName: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_NAME] ?: "Guest User"
    }

    val userEmail: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_EMAIL] ?: "guest@mindful.io"
    }

    val userPhotoUrl: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_PHOTO_URL]
    }

    suspend fun saveUser(id: String, name: String, email: String, photoUrl: String?) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = true
            preferences[USER_ID] = id
            preferences[USER_NAME] = name
            preferences[USER_EMAIL] = email
            if (photoUrl != null) {
                preferences[USER_PHOTO_URL] = photoUrl
            } else {
                preferences.remove(USER_PHOTO_URL)
            }
        }
    }

    suspend fun clearUser() {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = false
            preferences[USER_ID] = "guest"
            preferences[USER_NAME] = "Guest User"
            preferences[USER_EMAIL] = "guest@mindful.io"
            preferences.remove(USER_PHOTO_URL)
        }
    }
}
