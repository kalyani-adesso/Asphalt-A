package com.asphalt.android.utils

object Utils {
    fun generateFirebaseKey(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return buildString { repeat(20) { append(chars.random()) } }
    }
}