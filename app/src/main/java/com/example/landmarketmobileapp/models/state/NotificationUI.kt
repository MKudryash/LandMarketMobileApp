package com.example.landmarketmobileapp.models.state

import com.example.landmarketmobileapp.models.type.NotificationType

data class NotificationUI(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val isRead: Boolean = false,
    val type: NotificationType = NotificationType.INFO
)