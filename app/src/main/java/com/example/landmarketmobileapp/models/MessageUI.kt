package com.example.landmarketmobileapp.models

data class MessageUI(
    val id: String,
    val text: String,
    val time: String,
    val isSentByMe: Boolean,
    val isRead: Boolean = true,
    val type: MessageType = MessageType.TEXT
)