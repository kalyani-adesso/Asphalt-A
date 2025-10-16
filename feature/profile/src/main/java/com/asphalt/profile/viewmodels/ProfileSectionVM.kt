package com.asphalt.profile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.profile.repositories.ProfilesSectionRepo
import kotlinx.coroutines.launch

class ProfileSectionVM(val profilesSectionRepo: ProfilesSectionRepo) :
    ViewModel() {
    val profileData = profilesSectionRepo.profileData
    fun getProfileData(uid: String?) {
        viewModelScope.launch {
            profilesSectionRepo.getProfileData(uid)
        }
    }


}