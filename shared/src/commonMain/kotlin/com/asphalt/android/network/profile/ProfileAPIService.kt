package com.asphalt.android.network.profile

import com.asphalt.android.model.APIResult
import com.asphalt.android.model.GenericResponse
import com.asphalt.android.model.profile.BikeDTO
import com.asphalt.android.model.profile.ProfileDTO

interface ProfileAPIService {
    suspend fun getProfile(userId: String): APIResult<ProfileDTO>
    suspend fun editProfile(userId: String, profile: ProfileDTO): APIResult<GenericResponse>
    suspend fun addBike(userId: String, bike: BikeDTO): APIResult<GenericResponse>
    suspend fun deleteBike(userId: String, bikeId: String): APIResult<Unit>
    suspend fun getBikes(userId: String): APIResult<Map<String, BikeDTO>>
    suspend fun getBikeById(userId: String,bikeId: String): APIResult<BikeDTO>
}