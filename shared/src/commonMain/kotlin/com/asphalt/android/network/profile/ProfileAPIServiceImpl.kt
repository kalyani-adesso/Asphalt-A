package com.asphalt.android.network.profile


import com.asphalt.android.constants.APIConstants.USERS_URL
import com.asphalt.android.constants.APIConstants.BIKES_URL
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.GenericResponse
import com.asphalt.android.model.profile.BikeDTO
import com.asphalt.android.model.profile.ProfileDTO
import com.asphalt.android.network.KtorClient
import com.asphalt.android.network.BaseAPIService
import io.ktor.client.call.body

class ProfileAPIServiceImpl(client: KtorClient) : BaseAPIService(client),
ProfileAPIService {
    override suspend fun getProfile(userId: String): APIResult<ProfileDTO> {
        return  safeApiCall {
            get("$USERS_URL/$userId").body()
        }
    }

    override suspend fun editProfile(
        userId: String,
        profile: ProfileDTO
    ): APIResult<GenericResponse> {
        return  safeApiCall {
            patch(profile, "$USERS_URL/$userId").body()
        }
    }

    override suspend fun addBike(
        userId: String,
        bike: BikeDTO
    ): APIResult<GenericResponse> {
        return safeApiCall {
            post( bike, "$USERS_URL${BIKES_URL}").body()
        }
    }

    override suspend fun deleteBike(
        userId: String,
        bikeId: String
    ): APIResult<GenericResponse> {
        return safeApiCall {
            delete("$USERS_URL/$userId$BIKES_URL/$bikeId").body()
        }
    }

    override suspend fun getBikes(userId: String): APIResult<Map<String, BikeDTO>> {
        return  safeApiCall {
            get("$USERS_URL/$userId$BIKES_URL").body()
        }
    }
}