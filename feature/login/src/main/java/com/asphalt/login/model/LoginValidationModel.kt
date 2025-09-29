package com.asphalt.login.model

data class LoginValidationModel(
    var isShowEmailError: Boolean = false,
    var isShowPasswordError: Boolean = false,
    var emailError: String = "",
    var passwordError: String = ""
)
