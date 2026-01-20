package com.example.landmarketmobileapp.models.state

// UI State models
data class ChatUI(
    val id: String,
    val participantName: String,
    val participantAvatar: String? = null,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int = 0,
    val isOnline: Boolean = false,
    val advertisementTitle: String? = null,
    val advertisementPrice: Int? = null,
    val otherUserId: String
)