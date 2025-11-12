package com.asphalt.resetpassword.model

import com.asphalt.commonui.R

data class CreatePasswordStateModel(
    var email: String? = null,
    var password: String? = null,
    var confirmPassword: String? = null,
    var isShowEmailError: Boolean = false,
    var isShowPasswordError: Boolean = false,
    var isShowConfirmPasswordError: Boolean = false,
    var passwordError: Int = R.string.empty_string,
    var confirmPasswordError: Int = R.string.empty_string


)
