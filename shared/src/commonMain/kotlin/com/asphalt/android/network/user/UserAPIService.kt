package com.asphalt.android.network.user

import com.asphalt.android.model.APIResult
import com.asphalt.android.model.User

interface UserAPIService {
    suspend fun getAllUser(): APIResult<Map<String, User>>

}