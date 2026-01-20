package com.example.landmarketmobileapp.models.state

data class NotificationsState(
    val notifications: List<NotificationUI> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)