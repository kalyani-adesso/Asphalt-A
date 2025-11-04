package com.asphalt.android.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class AndroidLocationProvider(context: Context) : com.asphalt.android.location.LocationProvider{

    private val fusedClient : FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): LatLng? {
        return suspendCancellableCoroutine { cont ->
            try {
                fusedClient.lastLocation
                    .addOnSuccessListener { location ->
                        if (location != null)
                            cont.resume(LatLng(location.latitude,
                               location.longitude)
                            )
                        else cont.resume(null)
                    }
                    .addOnFailureListener { err ->
                        if (!cont.isCompleted) cont.resumeWithException(err)
                    }
            } catch (e: Exception) {
                cont.resume(null)
            }
        }
    }
}