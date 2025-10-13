package com.asphalt.dashboard.repository

import com.asphalt.android.datastore.DataStoreManager
import com.asphalt.android.model.CurrentUser
import com.asphalt.commonui.constants.PreferenceKeys
import kotlinx.serialization.json.Json

class DashBoardRepo(val dataStoreManager: DataStoreManager) {
    suspend fun getUserDetails(): CurrentUser? {
        val userDetails = dataStoreManager.getValue(PreferenceKeys.USER_DETAILS)
        return userDetails?.let {
            Json.decodeFromString<CurrentUser>(it)
        }
    }
}