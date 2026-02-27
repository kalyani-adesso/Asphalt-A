package com.asphalt.android.model.rides

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UrlNode(
    @SerialName("url")
    var url: String
)
