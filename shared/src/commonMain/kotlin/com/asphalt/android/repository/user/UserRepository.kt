package com.asphalt.android.repository.user

import com.asphalt.android.mapApiResult
import com.asphalt.android.mappers.toBikeListDomain
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.UserDomain
import com.asphalt.android.model.profile.ProfileDTO
import com.asphalt.android.network.user.UserAPIService

class UserRepository(private val apiService: UserAPIService) {
    suspend fun getAllUsers(): APIResult<List<UserDomain>> {
        return apiService.getAllUser().mapApiResult { response ->
            response?.toUserData().orEmpty()
        }
    }

    private fun Map<String, ProfileDTO>?.toUserData(): List<UserDomain>? {
        return this?.map { (id, profileDTO) ->
            val bikeDomain = profileDTO.bikes.toBikeListDomain().orEmpty().firstOrNull()
            UserDomain(
                uid = id,
                email = profileDTO.email,
                name = profileDTO.userName,
                profilePic = profileDTO.profilePicUrl,
                isMechanic = profileDTO.isMechanic,
                primaryBike = bikeDomain?.let { it.make  + " " + it.model  }.orEmpty(),
                contactNumber = profileDTO.phoneNumber
            )
        }
    }
}