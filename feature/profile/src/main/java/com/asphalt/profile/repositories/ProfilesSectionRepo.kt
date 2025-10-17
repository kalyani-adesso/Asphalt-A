package com.asphalt.profile.repositories

import com.asphalt.profile.data.ProfileData
import com.asphalt.profile.data.dummyProfiles
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfilesSectionRepo {
    private val _profileData = MutableStateFlow<ProfileData?>(null)
    val profileData: StateFlow<ProfileData?> = _profileData
    suspend fun getProfileData(uid: String?) {
        delay(200)
        uid?.let {
            _profileData.value =
                dummyProfiles.find { profileData -> profileData.uid == it } ?: dummyProfiles.first()
        }

    }

    suspend fun editProfileData(updatedProfileData: ProfileData) {
        delay(200)
        dummyProfiles = dummyProfiles.map { currentProfileData ->
            if (currentProfileData.uid == updatedProfileData.uid) updatedProfileData else currentProfileData
        }
        if (_profileData.value?.uid == updatedProfileData.uid) {
            _profileData.value = updatedProfileData
        }
    }
}