package com.example.landmarketmobileapp.models.state

data class MessagesState(
    val chatId: String,
    val messages: List<MessageUI> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: String? = "Пользователь"
)