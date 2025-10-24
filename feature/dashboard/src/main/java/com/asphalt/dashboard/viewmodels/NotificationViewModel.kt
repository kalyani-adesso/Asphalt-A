package com.asphalt.dashboard.viewmodels

import androidx.lifecycle.ViewModel
import com.asphalt.commonui.constants.Constants
import com.asphalt.dashboard.data.NotificationData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NotificationViewModel : ViewModel() {
    private val _notificationMutableStateFlow = MutableStateFlow(getNotification())
    val notificationState: StateFlow<ArrayList<NotificationData>> = _notificationMutableStateFlow.asStateFlow()


    fun getNotification(): ArrayList<NotificationData> {
        var notifcations = ArrayList<NotificationData>()
        val notification1 = NotificationData(
            id = 1,
            title = "Ride Reminder",
            description = "Your ride  \"Morning city ride\" starts in 30 minutes",
            isRead = true,
            date = "5 min ago",
            notificationType = Constants.RIDE_REMINDER
        )

        val notification2 = NotificationData(
            id = 2,
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
    fun updateReadStatus(id: Int) {
        val currentList = _notificationMutableStateFlow.value

        val updatedList = currentList.map { notification ->
            if (notification.id == id) {
                // Create a copy of the notification with isRead set to true
                notification.copy(isRead = true)
            } else {
                notification
            }
        }

        _notificationMutableStateFlow.value = ArrayList(updatedList)
    }

}