package com.asphalt.android.location

import com.google.android.gms.maps.model.LatLng

interface LocationProvider {

    suspend fun getCurrentLocation() : LatLng?
}