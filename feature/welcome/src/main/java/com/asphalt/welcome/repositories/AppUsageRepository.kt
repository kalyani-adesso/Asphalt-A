package com.asphalt.welcome.repositories

import com.asphalt.android.datastore.DataStoreManager
import com.asphalt.commonui.constants.PreferenceKeys


class AppUsageRepository(val dataStoreManager: DataStoreManager) {


    suspend fun getIsGetStartedDone(): Boolean {
        val isGetStartedDone = dataStoreManager.getBoolean(PreferenceKeys.IS_GET_STARTED_DONE)
        return isGetStartedDone ?: false
    }


    suspend fun registerGetStarted(value: Boolean) {
        dataStoreManager.saveValue(PreferenceKeys.IS_GET_STARTED_DONE, value)
    }
}