package com.asphalt.commonui.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream
import androidx.core.graphics.scale

object ImageUtils {
    fun uriToBase64Blob(context: Context, uri: Uri, maxWidth: Int = 1024): String? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                // 1. Decode with bounds to avoid OutOfMemory errors
                val options = BitmapFactory.Options().apply { inJustDecodeBounds = false }
                val originalBitmap = BitmapFactory.decodeStream(inputStream, null, options) ?: return null

                // 2. Scale down the image (High-res phone photos are too big for RTDB)
                val ratio = originalBitmap.width.toFloat() / originalBitmap.height.toFloat()
                val targetHeight = (maxWidth / ratio).toInt()
                val scaledBitmap = originalBitmap.scale(maxWidth, targetHeight)

                // 3. Compress to JPEG
                val outputStream = ByteArrayOutputStream()
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
                val bytes = outputStream.toByteArray()

                // 4. Encode to Base64 (The "BLOB")
                Base64.encodeToString(bytes, Base64.NO_WRAP)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    fun decodeBase64ToBitmap(base64String: String): Bitmap? {
        return try {
            val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        } catch (e: Exception) {
            null
        }
    }
}