package com.asphalt.android.repository.profile

import androidx.compose.ui.text.AnnotatedString
import com.asphalt.android.mapApiResult
import com.asphalt.android.network.profile.ProfileAPIService
import com.asphalt.android.model.APIResult
import com.asphalt.android.model.GenericResponse
import com.asphalt.android.model.profile.BikeDTO
import com.asphalt.android.model.profile.ProfileDTO
import com.asphalt.android.model.profile.ProfileDomain
import com.asphalt.android.model.profile.BikeDomain
import com.asphalt.android.viewmodel.AuthViewModel
import kotlin.collections.component1
import kotlin.collections.component2

class ProfileRepository(private val apiService: ProfileAPIService) {
    internal fun ProfileDTO.toProfileDomain(): ProfileDomain {
        return ProfileDomain(
            userName = userName,
            email = email,
            phoneNumber = phoneNumber,
            emergencyContact = emergencyContact,
            drivingLicense = drivingLicense,
            isMechanic = isMechanic,
            bikes = bikes?.map { (key, value) -> value.toBikeDomain(key) }.orEmpty()
        )
    }
    internal fun BikeDTO.toBikeDomain(id: String): BikeDomain {
        return BikeDomain(
            bikeId = bikeId.ifEmpty { id },
            type = bikeType,
            make = make,
            model = model
        )
    }

    suspend fun getProfile(userId: String): APIResult<ProfileDomain> {
        return apiService.getProfile(userId).mapApiResult { profileDTO ->
            profileDTO.toProfileDomain()
        }
    }

    private fun Map<String, BikeDTO>?.toBikeListDomain(): List<BikeDomain>? {
        return this?.map { (id, bikeDto) ->
           bikeDto.toBikeDomain(id)
        }
    }

    suspend fun getBikes(
        userId: String): APIResult<List<BikeDomain>> {
        return apiService.getBikes(userId).mapApiResult { bikesMap ->
         bikesMap.toBikeListDomain().orEmpty()
        }
    }

    suspend fun deleteBike(
        userId: String,
        bikeId: String): APIResult<GenericResponse> {
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
    ): APIResult<GenericResponse> {
        val profileDto = ProfileDTO(
            userName = userName,
            email = email,
           phoneNumber = contactNumber,
            emergencyContact = emergencyContact,
            drivingLicense = drivingLicense,
            isMechanic = isMechanic
            )
        return apiService.editProfile(userId, profileDto).mapApiResult {
            it
        }
    }

    suspend fun addBike(
        userId: String,
        bikeId: String,
        bikeType: String,make: String,
        model: String): APIResult<GenericResponse> {
        val bikeDto = BikeDTO(
            bikeId = bikeId,
            bikeType = bikeType,
            make = make,
            model = model
        )
        return apiService.addBike(userId, bikeDto).mapApiResult { response ->
            response
        }
    }
}
