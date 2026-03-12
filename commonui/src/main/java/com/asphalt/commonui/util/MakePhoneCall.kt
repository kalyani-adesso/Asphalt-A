package com.asphalt.commonui.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri

object PhoneCallUtils {
    fun dialPhoneNumber(context: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = "tel:$phoneNumber".toUri()
        }
        context.startActivity(intent)
    }
}