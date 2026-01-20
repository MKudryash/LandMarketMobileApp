package com.example.landmarketmobileapp.models

data class NotificationUI(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val isRead: Boolean = false,
    val type: NotificationType = NotificationType.INFO
)