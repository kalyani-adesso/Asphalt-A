package com.asphalt.android.repository.user

import com.asphalt.android.mapApiResult
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.User
import com.asphalt.android.model.UserData
import com.asphalt.android.network.user.UserAPIService

class UserRepository(private val apiService: UserAPIService) {
    suspend fun getAllUsers(): APIResult<List<UserData>> {
        return apiService.getAllUser().mapApiResult { response ->
            response?.toUserData().orEmpty()
        }
    }

    private fun Map<String, User>?.toUserData(): List<UserData>? {
        return this?.map { (id, rawQuery) ->
            UserData(
                uid = id,
                email = rawQuery.email ?: "",
                name = rawQuery.name ?: ""
            )
        }
    }
}