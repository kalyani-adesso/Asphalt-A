package com.asphalt.welcome.viewmodels

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import com.asphalt.commonui.utils.Constants
import com.asphalt.welcome.repositories.AppUsageRepository
import kotlinx.coroutines.flow.Flow


class WelcomeViewModel(private val appUsageRepository: AppUsageRepository) : ViewModel() {

    val isFirstTimeAppUsed: Flow<Boolean> = appUsageRepository.isFirstTimeAppUsed

    suspend fun registerGetStarted() {
        appUsageRepository.setFirstTimeUsed(true)
    }
}