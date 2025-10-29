package com.asphalt.android.viewmodel

import com.asphalt.android.repository.FirebaseApi
import com.asphalt.android.model.BikeInfo
import com.asphalt.android.model.ProfileInfo

class ProfileKMPViewModel(private val api: FirebaseApi) {

    suspend fun createOrEditProfile(
        userId: String,
        profile: ProfileInfo,
        isEdit: Boolean,
        email: String,
        userName: String
    ) {
        if (isEdit) api.editProfile(userId, email, userName,profile)
        else api.createProfile(userId, profile)
    }

    suspend fun addBike(userId: String, bike: BikeInfo) {
        api.addBike(userId, bike)
    }

    suspend fun deleteBike(userId: String, bikeId: String) {
        api.deleteBike(userId, bikeId)
    }

    suspend fun getProfile(userId: String): ProfileInfo? {
        return api.getProfile(userId)
    }

    suspend fun getBikes(userId: String): List<BikeInfo> {
        return api.getBikes(userId)
    }
}
