package com.asphalt.dashboard.data

data class NotificationData(
    val id:Int,
    var title: String? = null,
    var description: String? = null,
    var isRead: Boolean = false,
    var date: String? = null,
    var notificationType: Int
)