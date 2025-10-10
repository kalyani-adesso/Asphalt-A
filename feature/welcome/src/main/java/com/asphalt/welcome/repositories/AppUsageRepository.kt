package com.asphalt.welcome.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.asphalt.commonui.constants.Constants
import com.asphalt.commonui.constants.PreferenceKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.DATA_STORE_NAME)

class AppUsageRepository(val context: Context) {
    val isGetStartedDone: Flow<Boolean> = context.dataStore.data
        .map { prefs ->
            prefs[booleanPreferencesKey(PreferenceKeys.IS_GET_STARTED_DONE)] ?: false
        }

    suspend fun registerGetStarted(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(PreferenceKeys.IS_GET_STARTED_DONE)] = value
        }
    }
}