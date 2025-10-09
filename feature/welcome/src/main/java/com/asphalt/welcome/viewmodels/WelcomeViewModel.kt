package com.asphalt.welcome.viewmodels

import androidx.lifecycle.ViewModel
import com.asphalt.welcome.repositories.AppUsageRepository
import kotlinx.coroutines.flow.Flow


class WelcomeViewModel(private val appUsageRepository: AppUsageRepository) : ViewModel() {

    val isGetStartedDone: Flow<Boolean> = appUsageRepository.isGetStartedDone

    suspend fun registerGetStarted() {
        appUsageRepository.registerGetStarted(true)
    }
}