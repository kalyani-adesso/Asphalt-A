package com.asphalt.android.model.places

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceData(
    @SerialName("place_id") val placeId: Long? = null,
    @SerialName("licence") val licence: String? = null,
    @SerialName("osm_type") val osmType: String? = null,
    @SerialName("osm_id") val osmId: Long? = null,
    @SerialName("lat") val lat: Double? = null,
    @SerialName("lon") val lon: Double? = null,
    @SerialName("class") val clazz: String? = null,  // 'class' is a reserved keyword
    @SerialName("type") val type: String? = null,
    @SerialName("place_rank") val placeRank: Int? = null,
    @SerialName("importance") val importance: Double? = null,
    @SerialName("addresstype") val addressType: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("display_name") val displayName: String? = null,
    @SerialName("boundingbox") val boundingBox: List<String>? = null
)

@Serializable
data class PhotonResponse(
    val features: List<PhotonFeature>
)

@Serializable
data class PhotonFeature(
    val geometry: PhotonGeometry,
    val properties: PhotonProperties
)

@Serializable
data class PhotonGeometry(
    val coordinates: List<Double> // Note: GeoJSON is always [longitude, latitude]
)

@Serializable
data class PhotonProperties(
    val name: String? = null,
    val city: String? = null,
    val state: String? = null,
    val country: String? = null,
    val street: String? = null
) {
    fun getFormattedAddress(): String {
        val parts = listOfNotNull(name, street, city, state)
        return parts.joinToString(", ")
    }
    fun getDisplayName(): String{
        val parts = listOfNotNull(name, city, state)
        return parts.joinToString(", ")
    }
}