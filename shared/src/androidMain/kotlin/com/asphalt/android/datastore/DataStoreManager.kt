package com.asphalt.android.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.asphalt.commonui.constants.Constants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {
    private val Context.dataStore by preferencesDataStore(name = Constants.DATA_STORE_NAME)

    suspend fun saveValue(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[dataStoreKey] = value
        }
    }

    suspend fun saveValue(key: String, value: Boolean) {
        val dataStoreKey = booleanPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[dataStoreKey] = value
        }
    }

    suspend fun getBoolean(key: String): Boolean? {
        val dataStoreKey = booleanPreferencesKey(key)
        return context.dataStore.data
            .map { it[dataStoreKey] }
            .first()
    }

    suspend fun getValue(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        return context.dataStore.data
            .map { it[dataStoreKey] }
            .first()
    }

}