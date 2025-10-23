package com.asphalt.dashboard.viewmodels

import androidx.lifecycle.ViewModel
import com.asphalt.commonui.constants.Constants
import com.asphalt.dashboard.data.NotificationData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NotificationViewModel : ViewModel() {
    private val _notificationMutableStateFlow = MutableStateFlow(getNotification())
    val notificationState: StateFlow<ArrayList<NotificationData>> = _notificationMutableStateFlow


    fun getNotification(): ArrayList<NotificationData> {
        var notifcations = ArrayList<NotificationData>()
        val notification1 = NotificationData(
            title = "Ride Reminder",
            description = "Your ride  \"Morning city ride\" starts in 30 minutes",
            isRead = true,
            date = "5 min ago",
            notificationType = Constants.RIDE_REMINDER
        )

        val notification2 = NotificationData(
            title = "New Rider Joined",
            description = "Sooraj joined to  \"Weekend Adventure ride\"",
            isRead = false,
            date = "1 hour ago",
            notificationType = Constants.RIDER_JOINED
        )
        notifcations.add(notification1)
        notifcations.add(notification2)
        return notifcations;

    }

}