package com.asphalt.profile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.profile.data.ProfileData
import com.asphalt.profile.repositories.ProfilesSectionRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileSectionVM(val profilesSectionRepo: ProfilesSectionRepo) :
    ViewModel() {
    private val _profileData = MutableStateFlow<ProfileData?>(null)
    val profileData: StateFlow<ProfileData?> = _profileData
    fun getProfileData(uid: String?) {
        viewModelScope.launch {
            _profileData.value = profilesSectionRepo.getProfileData(uid)
        }
    }


}