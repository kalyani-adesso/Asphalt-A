package com.asphalt.android.repository

import com.asphalt.android.datastore.DataStoreManager
import com.asphalt.android.model.CurrentUser
import com.asphalt.commonui.constants.PreferenceKeys
import kotlinx.serialization.json.Json


actual class UserRepoImpl actual constructor() : UserRepo {

    private lateinit var dataStoreManager: DataStoreManager
    fun setDataStoreManager(dataStoreManager: DataStoreManager) {
        this.dataStoreManager = dataStoreManager
    }

    actual override suspend fun getUserDetails(): CurrentUser? {
        val initialized = ::dataStoreManager.isInitialized
        if (initialized) {
            val userDetails = dataStoreManager.getValue(PreferenceKeys.USER_DETAILS)
            return userDetails?.let {
                if (it.isNotEmpty()) {
                    Json.decodeFromString<CurrentUser>(it)
                } else null
            }
        } else
            return null
    }


}