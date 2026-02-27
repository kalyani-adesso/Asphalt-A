package com.asphalt.joinaride.services

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.asphalt.android.PlatformDatabase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


class CurrentLocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val database = PlatformDatabase() // Your Firebase/KMP wrapper
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var lastLocation: Location? = null
    private var totalDistanceMetres: Float = 0f
    private var rideId: String? = null
    private var ongoingRideId: String? = null

    companion object {
        const val CHANNEL_ID = "ride_tracking_channel"
        const val NOTIFICATION_ID = 101
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        rideId = intent?.getStringExtra("RIDE_ID")
        ongoingRideId = intent?.getStringExtra("ONGOING_RIDE_ID")

        // 1. Build the notification
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Ride Active")
            .setContentText("Sharing live location with your group...")
            .setSmallIcon(R.drawable.ic_menu_mylocation)
            .setOngoing(true)
            .setCategory(Notification.CATEGORY_SERVICE)
//            .setForegroundServiceBehavior(Notification.FOREGROUND_SERVICE_IMMEDIATE)
            .build()

        // 2. Start Foreground (Handle Android 14+ types)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }

        // 3. Start the GPS updates
        startLocationUpdates()

        return START_STICKY
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
//            .setMinUpdateDistanceMeters(5f) // Only update if moved 5 meters to save battery
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val currentLocation = result.lastLocation ?: return

                lastLocation?.let { previous ->
                    val distanceBetween = previous.distanceTo(currentLocation)

                    if (currentLocation.accuracy < 20 && distanceBetween > 2.0) {
                        totalDistanceMetres += distanceBetween
                    }
                }

                lastLocation = currentLocation

                var speedInMps: Float
                var speedInKph = 0.0
                if (currentLocation.hasSpeed()) {
                    speedInMps = currentLocation.speed
                    speedInKph = speedInMps * 3.6
                }
                saveToFirebase(
                    currentLocation.latitude,
                    currentLocation.longitude,
                    speedInKph,
                    totalDistanceMetres
                )
            }
        }

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (unlikely: SecurityException) {
            // Log error: Lost location permission
        }
    }

    private fun saveToFirebase(
        lat: Double,
        lng: Double,
        speedInKph: Double,
        totalDistanceMetres: Float
    ) {
        val rId = rideId ?: return
        val uId = ongoingRideId ?: return
        val roundedSpeed = (speedInKph * 100).roundToInt() / 100.0
        var status = "connected"
        if (roundedSpeed < 1) {
            status = "stopped"
        } else if (roundedSpeed < 10)
            status = "delayed"

        serviceScope.launch {
            val data = mapOf(
                "currentLat" to lat,
                "currentLong" to lng,
                "speedInKph" to roundedSpeed,
                "status" to status,
                "totalDistance" to totalDistanceMetres / 1000
//                "timestamp" to System.currentTimeMillis()
            )
            database.getReference("ongoing_ride/$rId/$uId").updateChildren(data)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Ride Tracking Service",
                NotificationManager.IMPORTANCE_LOW // Low ensures no annoying sound every 5 seconds
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        Log.d("service?", "stopped")
        serviceScope.cancel()
        super.onDestroy()
    }
}
