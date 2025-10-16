package com.asphalt.profile.repositories

import com.asphalt.profile.data.ProfileData
import com.asphalt.profile.data.dummyProfiles
import kotlinx.coroutines.delay

class ProfilesSectionRepo {
    suspend fun getProfileData(uid: String?): ProfileData? {
        delay(200)
        uid?.let {
            return dummyProfiles.find { profileData -> profileData.uid == it }
        }
        return null
    }
}