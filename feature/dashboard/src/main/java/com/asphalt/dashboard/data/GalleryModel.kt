package com.asphalt.dashboard.data

import android.net.Uri
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class GalleryModel(
    @Contextual var uri: Uri,
    var isFromLocal: Boolean = false,
    var imageString: String? = null
)