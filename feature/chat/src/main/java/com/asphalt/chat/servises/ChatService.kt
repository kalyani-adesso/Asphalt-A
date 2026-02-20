package com.asphalt.chat.servises
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.asphalt.android.PlatformDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class ChatService : Service() {

    private var listener: ListenerRegistration? = null
    private val CHANNEL_ID_FOREGROUND = "FirestoreServiceChannel"
    private val CHANNEL_ID_LOCAL = "ChatLocalChannel"
    private val FOREGROUND_NOTIFICATION_ID = 1
    private var localNotificationId = 2

    override fun onCreate() {
        super.onCreate()
        createForegroundNotificationChannel()
        createLocalNotificationChannel()

        // Start foreground service
        startForeground(
            FOREGROUND_NOTIFICATION_ID,
            buildForegroundNotification("Chat service running")
        )

    }

    override fun onDestroy() {
        super.onDestroy()
        listener?.remove()
        Log.d("ChatService", "Service destroyed and listener removed")
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createForegroundNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID_FOREGROUND,
                "Foreground Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    private fun createLocalNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID_LOCAL,
                "Chat Notifications",
                NotificationManager.IMPORTANCE_HIGH // HIGH ensures pop-up in emulator
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    private fun buildForegroundNotification(content: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID_FOREGROUND)
            .setContentTitle("Chat Service Running")
            .setContentText(content)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setOngoing(true)
            .build()
    }


}