package com.asphalt.commonui.util

object EmailValidator {
    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()

    fun isValid(email: String?): Boolean {
        if (email.isNullOrBlank()) return false
        return emailRegex.matches(email)
    }
}