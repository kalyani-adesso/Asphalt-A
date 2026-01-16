package com.asphalt.android

actual object Logger {
    actual fun d(tag: String, message: String) {
        android.util.Log.d(tag, message)
    }
}