package com.asphalt.android.repository.profile

import com.asphalt.android.mapApiResult
import com.asphalt.android.mappers.toBikeDomain
import com.asphalt.android.mappers.toBikeListDomain
import com.asphalt.android.mappers.toProfileDomain
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.GenericResponse
import com.asphalt.android.model.profile.BikeDTO
import com.asphalt.android.model.profile.BikeDomain
import com.asphalt.android.model.profile.EditProfileRequestDTO
import com.asphalt.android.model.profile.ProfileDomain
import com.asphalt.android.network.profile.ProfileAPIService

class ProfileRepository(private val apiService: ProfileAPIService) {


    suspend fun getProfile(userId: String): APIResult<ProfileDomain> {
        return apiService.getProfile(userId).mapApiResult { profileDTO ->
            profileDTO.toProfileDomain(userId)
        }
    }


    suspend fun getBikes(
        userId: String
    ): APIResult<List<BikeDomain>> {
        return apiService.getBikes(userId).mapApiResult { bikesMap ->
            bikesMap.toBikeListDomain().orEmpty()
        }
    }

    suspend fun deleteBike(
        userId: String,
        bikeId: String
    ): APIResult<Unit> {
        return apiService.deleteBike(userId, bikeId)
    }

    suspend fun editProfile(
        userId: String,
        userName: String,
        email: String,
        contactNumber: String,
        emergencyContact: String,
        drivingLicense: String,
        isMechanic: Boolean
    ): APIResult<Unit> {
        val profileDto = EditProfileRequestDTO(
            userName = userName,
            email = email,
            phoneNumber = contactNumber,
            emergencyContact = emergencyContact,
            drivingLicense = drivingLicense,
            isMechanic = isMechanic
        )
        return apiService.editProfile(userId, profileDto).mapApiResult {
        }
    }

    suspend fun addBike(
        userId: String,
        bikeType: Int, make: String,
        model: String
    ): APIResult<GenericResponse> {
        val bikeDto = BikeDTO(
            bikeType = bikeType,
            make = make,
            model = model
        )
        return apiService.addBike(userId, bikeDto).mapApiResult {
            it
        }
    }

    suspend fun getBikeById(bikeId: String, userUID: String): APIResult<BikeDomain> {
        return apiService.getBikeById(userId = userUID, bikeId).mapApiResult {
            it.toBikeDomain(bikeId)
        }
    }
}
