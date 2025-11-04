package com.asphalt.profile.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asphalt.android.helpers.APIHelperUI
import com.asphalt.android.repository.profile.ProfileRepository
import com.asphalt.commonui.util.EmailValidator
import com.asphalt.profile.data.ProfileUIModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EditProfileVM(val profileRepository: ProfileRepository) : ViewModel() {

    private val _editFullName = MutableStateFlow("")
    private val _editEmail = MutableStateFlow("")
    private val _editPhoneNumber = MutableStateFlow("")
    private val _editLicense = MutableStateFlow("")
    private val _editMechanic = MutableStateFlow(false)
    private val _editEmergencyNo =
        MutableStateFlow("")
    val editFullName: StateFlow<String> = _editFullName.asStateFlow()
    val editEmail: StateFlow<String> = _editEmail.asStateFlow()
    val editPhoneNumber: StateFlow<String> = _editPhoneNumber.asStateFlow()
    val editLicense: StateFlow<String> = _editLicense.asStateFlow()
    val editMechanic: StateFlow<Boolean> = _editMechanic.asStateFlow()
    val editEmergencyNo: StateFlow<String> = _editEmergencyNo.asStateFlow()

    // Full Name Error
    private val _fullNameError = MutableStateFlow(false)
    val fullNameError: StateFlow<Boolean> = _fullNameError.asStateFlow()

    // Email Error
    private val _emailError = MutableStateFlow(false)
    val emailError: StateFlow<Boolean> = _emailError.asStateFlow()

    // Phone Number Error
    private val _phoneNumberError = MutableStateFlow(false)
    val phoneNumberError: StateFlow<Boolean> = _phoneNumberError.asStateFlow()

    // Driving License Error
    private val _licenseError = MutableStateFlow(false)
    val licenseError: StateFlow<Boolean> = _licenseError.asStateFlow()

    // Emergency Contact Number Error
    private val _emergencyNoError = MutableStateFlow(false)
    val emergencyNoError: StateFlow<Boolean> = _emergencyNoError.asStateFlow()

    // Full Name Update
    fun updateFullName(input: String) {
        _fullNameError.value = false
        _editFullName.value = input
    }

    // Email Update
    fun updateEmail(input: String) {
        _emailError.value = false
        _editEmail.value = input
    }

    // Phone Number Update
    fun updatePhoneNumber(input: String) {
        _phoneNumberError.value = false
        _editPhoneNumber.value = input
    }

    // Driving License Update
    fun updateLicense(input: String) {
        _licenseError.value = false
        _editLicense.value = input
    }

    // Emergency Contact Number Update
    fun updateEmergencyNo(input: String) {
        _emergencyNoError.value = false
        _editEmergencyNo.value = input
    }

    fun updateMechanic(isMechanic: Boolean) {
        _editMechanic.value = isMechanic
    }


    fun validateProfileFields(): Boolean {
        var isValid = true

        // Check Full Name
        if (_editFullName.value.isEmpty()) {
            _fullNameError.value = true
            isValid = false
        }

        // Check Email
        if (_editEmail.value.isEmpty() or !EmailValidator.isValid(_editEmail.value)) {
            _emailError.value = true
            isValid = false
        }

        // Check Phone Number
        if (_editPhoneNumber.value.isEmpty()) {
            _phoneNumberError.value = true
            isValid = false
        }

        // Check Driving License
        if (_editLicense.value.isEmpty()) {
            _licenseError.value = true
            isValid = false
        }

        // Check Emergency Contact Number
        if (_editEmergencyNo.value.isEmpty()) {
            _emergencyNoError.value = true
            isValid = false
        }


        return isValid
    }




    fun setCurrentProfile(profileUIModel: ProfileUIModel?) {
        _editMechanic.value = profileUIModel?.isMechanic ?: false
        _editEmail.value = profileUIModel?.userEmail ?: ""
        _editLicense.value = profileUIModel?.drivingLicenseNumber ?: ""
        _editEmergencyNo.value = profileUIModel?.emergencyContactNumber ?: ""
        _editPhoneNumber.value = profileUIModel?.phoneNumber ?: ""
        _editFullName.value = profileUIModel?.username ?: ""
    }

}