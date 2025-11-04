package com.asphalt.android.model.places

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceData(
    @SerialName("place_id") val placeId: Long? = null,
    @SerialName("licence") val licence: String? = null,
    @SerialName("osm_type") val osmType: String? = null,
    @SerialName("osm_id") val osmId: Long? = null,
    @SerialName("lat") val lat: String? = null,
    @SerialName("lon") val lon: String? = null,
    @SerialName("class") val clazz: String? = null,  // 'class' is a reserved keyword
    @SerialName("type") val type: String? = null,
    @SerialName("place_rank") val placeRank: Int? = null,
    @SerialName("importance") val importance: Double? = null,
    @SerialName("addresstype") val addressType: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("display_name") val displayName: String? = null,
    @SerialName("boundingbox") val boundingBox: List<String>? = null
)