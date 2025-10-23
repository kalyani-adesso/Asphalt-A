package com.asphalt.profile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.profile.repositories.ProfilesDataRepo
import kotlinx.coroutines.launch

class ProfileSectionVM(val profilesDataRepo: ProfilesDataRepo) :
    ViewModel() {
    val profileData = profilesDataRepo.profileData
    fun getProfileData(uid: String?) {
        viewModelScope.launch {
            profilesDataRepo.getProfileData(uid)
        }
    }


}