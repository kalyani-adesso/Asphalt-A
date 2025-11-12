package com.asphalt.android.network.user

import com.asphalt.android.constants.APIConstants.USERS_URL
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.profile.ProfileDTO
import com.asphalt.android.network.BaseAPIService
import com.asphalt.android.network.KtorClient
import io.ktor.client.call.body

class UserAPIServiceImpl(client: KtorClient) : BaseAPIService(client), UserAPIService {
    override suspend fun getAllUser(): APIResult<Map<String, ProfileDTO>> {
        return safeApiCall {
            get(USERS_URL).body()
        }
    }
}