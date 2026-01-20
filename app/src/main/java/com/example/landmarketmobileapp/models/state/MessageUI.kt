package com.example.landmarketmobileapp.models.state

import com.example.landmarketmobileapp.models.type.MessageType

data class MessageUI(
    val id: String,
    val text: String,
    val time: String,
    val isSentByMe: Boolean,
    val isRead: Boolean = true,
    val type: MessageType = MessageType.TEXT
)