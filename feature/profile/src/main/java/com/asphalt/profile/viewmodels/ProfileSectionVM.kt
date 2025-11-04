package com.asphalt.profile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.repository.profile.ProfileRepository
import com.asphalt.android.viewmodels.AndroidUserVM
import com.asphalt.profile.data.ProfileUIModel
import com.asphalt.profile.mapper.toCurrentUserModel
import com.asphalt.profile.mapper.toProfileUIModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileSectionVM(val profileRepository: ProfileRepository, val androidUserVM: AndroidUserVM) :
    ViewModel() {

    private val _profileData = MutableStateFlow<ProfileUIModel?>(null)
    val profileData = _profileData.asStateFlow()
    private val userUID: String
        get() = androidUserVM.getCurrentUserUID()

    fun getProfileData(isUpdate: Boolean = false) {
        viewModelScope.launch {
            APIHelperUI.handleApiResult(APIHelperUI.runWithLoader {
                profileRepository.getProfile(
                    userUID
                )
            }, this) {
                val profileUIModel = it.toProfileUIModel()
                _profileData.value = profileUIModel
                if (isUpdate)
                    androidUserVM.updateUserData(profileUIModel.toCurrentUserModel())
            }

        }
    }

    fun editProfile(
        userName: String,
        email: String,
        contact: String,
        emergencyNumber: String,
        licenseNo: String,
        isMechanic: Boolean
    ) {
        viewModelScope.launch {
            userUID.run {
                APIHelperUI.handleApiResult(
                    APIHelperUI.runWithLoader {
                        profileRepository.editProfile(
                            this, userName,
                            email, contact,
                            emergencyNumber,
                            licenseNo,
                            isMechanic
                        )
                    }, viewModelScope
                ) {
                    getProfileData(isUpdate = true)
                }

            }
        }
    }


}