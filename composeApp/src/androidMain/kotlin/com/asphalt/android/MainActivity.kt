package com.asphalt.android

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.asphalt.android.navigation.NavigationRoot
import com.asphalt.commonui.theme.AsphaltTheme
import com.asphalt.profile.screens.ProfileScreen
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.inc

class MainActivity : ComponentActivity() {
    private val CHANNEL_ID_LOCAL = "local_notifications"
    private var localNotificationId = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        chats()

        setContent {
            AsphaltTheme {
                Surface {
                    NavigationRoot()
//                    ProfileScreen()
                }
            }
        }
    }
    fun chats(){
        val database = FirebaseDatabase.getInstance()
        val chatsRef = database.getReference("chats")

        // Real-time listener for any change in "chats"
        chatsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Trigger a notification on every change
                createNotificationChannel()
                pushLocalNotification("Database Changed", "A change occurred in chats!")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainActivity", "Error reading chats: ${error.message}")
            }
        })
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Local Notifications"
            val descriptionText = "Channel for local notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID_LOCAL, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun pushLocalNotification(title: String, content: String) {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID_LOCAL)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(this)
            .notify(localNotificationId++, notification)
    }
}


@Preview
@Composable
fun AppAndroidPreview() {
    AsphaltTheme {
        // AsphaltApp()
        NavigationRoot()
    }
}