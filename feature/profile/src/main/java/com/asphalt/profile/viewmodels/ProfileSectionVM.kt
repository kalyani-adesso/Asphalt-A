package com.asphalt.profile.viewmodels

import androidx.lifecycle.ViewModel
import com.asphalt.profile.repositories.ProfileSectionRepo

class ProfileSectionVM(
    profileSectionRepo: ProfileSectionRepo,
) :
    ViewModel() {
    suspend fun getProfileData(uid: String?) {

    }

}