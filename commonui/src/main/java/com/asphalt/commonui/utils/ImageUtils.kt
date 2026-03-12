package com.asphalt.commonui.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import androidx.core.graphics.scale
import com.google.android.gms.maps.model.BitmapDescriptor
import androidx.core.graphics.createBitmap
import com.google.android.gms.maps.model.BitmapDescriptorFactory

object ImageUtils {

    fun bitmapDescriptorFromVector(
        context: Context,
        vectorResId: Int
    ): BitmapDescriptor? {
        val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        val bm = createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)
        val canvas = android.graphics.Canvas(bm)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bm)
    }
    fun uriToBase64Blob(context: Context, uri: Uri, maxWidth: Int = 1024): String? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val options = BitmapFactory.Options().apply { inJustDecodeBounds = false }
                val originalBitmap = BitmapFactory.decodeStream(inputStream, null, options) ?: return null

                val ratio = originalBitmap.width.toFloat() / originalBitmap.height.toFloat()
                val targetHeight = (maxWidth / ratio).toInt()
                val scaledBitmap = originalBitmap.scale(maxWidth, targetHeight)

                val outputStream = ByteArrayOutputStream()
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
                val bytes = outputStream.toByteArray()

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