package com.asphalt.android.network.user

import com.asphalt.android.constants.APIConstants.USER_URL
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.User
import com.asphalt.android.network.BaseAPIService
import com.asphalt.android.network.KtorClient
import io.ktor.client.call.body

class UserAPIServiceImpl(client: KtorClient) : BaseAPIService(client), UserAPIService {
    override suspend fun getAllUser(): APIResult<Map<String, User>> {
        return safeApiCall {
            get(USER_URL).body()
        }
    }
}