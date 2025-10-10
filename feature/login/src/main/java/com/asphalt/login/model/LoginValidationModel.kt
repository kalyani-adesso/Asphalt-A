package com.asphalt.login.model

import com.asphalt.commonui.R

data class LoginValidationModel(
    var isShowEmailError: Boolean = false,
    var isShowPasswordError: Boolean = false,
    var emailError: Int = R.string.empty_string,
    var passwordError: Int = R.string.empty_string
)
