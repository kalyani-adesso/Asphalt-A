package com.asphalt.joinaride.services

import android.app.*
import android.content.Intent
import android.content.pm.ServiceInfo
import android.location.Location
import android.os.Build
import android.os.HandlerThread
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.asphalt.android.PlatformDatabase
import com.google.android.gms.location.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlin.math.roundToInt

class CurrentLocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val database = PlatformDatabase()
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val locationHandlerThread = HandlerThread("LocationCallbackThread")
    private var locationCallback: LocationCallback? = null
    private var lastLocation: Location? = null
    private var totalDistanceMetres: Float = 0f
    private var rideId: String? = null
    private var ongoingRideId: String? = null
    private var lastValidBearing: Float = 0f

    companion object {
        const val CHANNEL_ID = "ride_tracking_channel"
        const val NOTIFICATION_ID = 101
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationHandlerThread.start()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        rideId = intent?.getStringExtra("RIDE_ID")
        ongoingRideId = intent?.getStringExtra("ONGOING_RIDE_ID")

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Ride Active")
            .setContentText("Sharing live location with your group...")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setOngoing(true)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }

        val rId = rideId
        val uId = ongoingRideId
        serviceScope.launch {
            if (rId != null && uId != null) {
                restoreDistanceFromFirebase(rId, uId)
            }
            withContext(Dispatchers.Main) {
                startLocationUpdates()
            }
        }

        // Re-delivers the last intent on system restart so rideId/ongoingRideId are never null
        return START_REDELIVER_INTENT
    }

    private suspend fun restoreDistanceFromFirebase(rideId: String, ongoingRideId: String) {
        try {
            val snapshot = database
                .getReference("ongoing_ride/$rideId/$ongoingRideId")
                .observeValue()
                .first()
            val data = snapshot.getValue() as? Map<*, *>
            val savedDistanceKm = (data?.get("totalDistance") as? Number)?.toFloat() ?: 0f
            totalDistanceMetres = savedDistanceKm * 1000f
            Log.d("CurrentLocationService", "Restored distance: ${savedDistanceKm}km")
        } catch (e: Exception) {
            Log.e("CurrentLocationService", "Failed to restore distance, starting from 0: ${e.message}")
        }
    }

    private fun startLocationUpdates() {
        if (locationCallback != null) return

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val currentLocation = result.lastLocation ?: return

                val calculatedBearing = if (currentLocation.hasBearing() && currentLocation.speed > 0.5f) {
                    currentLocation.bearing
                } else {
                    lastLocation?.let { previous ->
                        if (previous.distanceTo(currentLocation) > 1.0) {
                            (previous.bearingTo(currentLocation) + 360) % 360
                        } else {
                            lastValidBearing
                        }
                    } ?: lastValidBearing
                }

                lastValidBearing = calculatedBearing

                lastLocation?.let { previous ->
                    val distanceBetween = previous.distanceTo(currentLocation)
                    if (currentLocation.accuracy < 20 && distanceBetween > 2.0) {
                        totalDistanceMetres += distanceBetween
                    }
                }

                lastLocation = currentLocation

                val speedInKph = if (currentLocation.hasSpeed()) currentLocation.speed * 3.6 else 0.0

                saveToFirebase(
                    currentLocation.latitude,
                    currentLocation.longitude,
                    speedInKph,
                    totalDistanceMetres,
                    calculatedBearing
                )
            }
        }

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback!!,
                locationHandlerThread.looper
            )
        } catch (unlikely: SecurityException) {
            Log.e("CurrentLocationService", "Location permission revoked")
        }
    }

    private fun saveToFirebase(
        lat: Double,
        lng: Double,
        speedInKph: Double,
        totalDistanceMetres: Float,
        calculatedBearing: Float
    ) {
        val rId = rideId ?: return
        val uId = ongoingRideId ?: return
        val roundedSpeed = (speedInKph * 100).roundToInt() / 100.0

        val status = when {
            roundedSpeed < 1.0 -> "stopped"
            roundedSpeed < 10.0 -> "delayed"
            else -> "connected"
        }

        serviceScope.launch {
            val data = mapOf(
                "currentLat" to lat,
                "currentLong" to lng,
                "speedInKph" to roundedSpeed,
                "status" to status,
                "totalDistance" to totalDistanceMetres / 1000,
                "bearing" to calculatedBearing
            )
            database.getReference("ongoing_ride/$rId/$uId").updateChildren(data)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Ride Tracking Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
        locationCallback = null
        serviceScope.cancel()
        locationHandlerThread.quitSafely()
        super.onDestroy()
    }
}
