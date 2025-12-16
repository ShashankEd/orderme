package com.product.orderfromhere.model.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.datastore: DataStore<Preferences> by preferencesDataStore("app_data")

class DataStoreManager(private val context: Context) {
    companion object {
        val SESSION_TOKEN = stringPreferencesKey("session_token")
    }

    suspend fun saveSessionToken(token: String) {
        context.datastore.edit { preferences ->
            preferences[SESSION_TOKEN] = token
        }
    }

    // Flow to read the token from user preference
    val sessionToken: Flow<String> = context.datastore.data
        .map { preferences ->
            preferences[SESSION_TOKEN] ?: ""
        }
}