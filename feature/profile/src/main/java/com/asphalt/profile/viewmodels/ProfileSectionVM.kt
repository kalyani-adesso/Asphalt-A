package com.asphalt.profile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.repository.profile.ProfileRepository
import com.asphalt.profile.data.ProfileUIModel
import com.asphalt.profile.mapper.toProfileUIModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileSectionVM(val profileRepository: ProfileRepository) :
    ViewModel() {

    private val _profileData = MutableStateFlow<ProfileUIModel?>(null)
    val profileData = _profileData.asStateFlow()
    fun getProfileData(uid: String?) {
        viewModelScope.launch {
            APIHelperUI.handleApiResult(APIHelperUI.runWithLoader {
                profileRepository.getProfile(
                    uid ?: ""
                )
            }, this) {
                _profileData.value = it.toProfileUIModel()
            }

        }
    }

    fun editProfile(
        uid: String?,
        userName: String,
        email: String,
        contact: String,
        emergencyNumber: String,
        licenseNo: String,
        isMechanic: Boolean
    ) {
        viewModelScope.launch {
            uid?.let {
                APIHelperUI.handleApiResult(
                    APIHelperUI.runWithLoader {
                        profileRepository.editProfile(
                            it, userName,
                            email, contact,
                            emergencyNumber,
                            licenseNo,
                            isMechanic
                        )
                    }, viewModelScope
                ) {
                    getProfileData(uid)
                }

            }
        }
    }


}