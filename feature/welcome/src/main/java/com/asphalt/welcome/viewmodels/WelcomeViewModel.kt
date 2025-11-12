package com.asphalt.welcome.viewmodels

import androidx.lifecycle.ViewModel
import com.asphalt.commonui.constants.PreferenceKeys
import com.asphalt.welcome.repositories.AppUsageRepository
import kotlinx.coroutines.flow.Flow


class WelcomeViewModel(private val appUsageRepository: AppUsageRepository) : ViewModel() {

    suspend fun getIsGetStartedDone(): Boolean {
        return appUsageRepository.getIsGetStartedDone()
    }

    suspend fun registerGetStarted() {
        appUsageRepository.registerGetStarted(true)
    }
    suspend fun isRememberUser(): Boolean{
        return appUsageRepository.getIsRememberUser()
    }
}