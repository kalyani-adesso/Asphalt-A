package com.asphalt.android.repository

import com.asphalt.android.model.BikeInfo
import com.asphalt.android.model.ProfileInfo

actual class FirebaseApi {
    actual suspend fun createProfile(
        userId: String,
        profile: ProfileInfo
    ) {
    }
    
    actual suspend fun addBike(userId: String, bike: BikeInfo) {
    }

    actual suspend fun deleteBike(userId: String, bikeId: String) {
    }

    actual suspend fun getProfile(userId: String): ProfileInfo? {
        TODO("Not yet implemented")
    }

    actual suspend fun editProfile(userId: String, profile: ProfileInfo) {
    }

    actual suspend fun  getBikes(userId: String): List<BikeInfo> {
        TODO("Not yet implemented")
    }
}
