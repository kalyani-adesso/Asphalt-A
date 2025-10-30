package com.asphalt.android.repository

import com.asphalt.android.model.BikeInfo
import com.asphalt.android.model.ProfileInfo

expect class FirebaseApi {
    suspend fun createProfile(userId: String, profile: ProfileInfo)
    suspend fun editProfile(userId: String, profile: ProfileInfo)
    suspend fun addBike(userId: String, bike: BikeInfo)
    suspend fun deleteBike(userId: String, bikeId: String)
    suspend fun getProfile(userId: String): ProfileInfo?
    suspend fun  getBikes(userId: String): List<BikeInfo>
}