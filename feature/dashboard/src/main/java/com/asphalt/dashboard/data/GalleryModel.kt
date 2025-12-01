package com.asphalt.dashboard.data

import android.net.Uri
import kotlinx.serialization.Serializable

@Serializable
data class GalleryModel(var uri: String? = null, var isFromLocal: Boolean = false)