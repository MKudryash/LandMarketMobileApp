package com.example.landmarketmobileapp.models.state

import com.example.landmarketmobileapp.models.state.ChatUI

// Состояния UI
data class ChatListState(
    val chats: List<ChatUI> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)