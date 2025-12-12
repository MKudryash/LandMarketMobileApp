package com.example.landmarketmobileapp.models


import com.example.landmarketmobileapp.viewModels.Advertisement
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class Chat(
    @SerialName("id")
    val id: String,
    @SerialName("participant1_id")
    val user1Id: String,
    @SerialName("participant2_id")
    val user2Id: String,
    @SerialName("advertisement_id")
    val advertisementId: String? = null,
    @SerialName("last_message_id")
    val lastMessage: String? = null,
    @SerialName("last_message_at")
    val lastMessageTime: String? = null,
    @SerialName("unread_count_p1")
    val user1UnreadCount: Int = 0,
    @SerialName("unread_count_p2")
    val user2UnreadCount: Int = 0,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null,

    // Joined fields
    val otherUser: Profile? = null,
    val advertisement: Advertisement? = null
)

@Serializable
data class ChatMessage(
    @SerialName("id")
    val id: String,
    @SerialName("chat_id")
    val chatId: String,
    @SerialName("sender_id")
    val senderId: String,
    @SerialName("content")
    val message: String,
    @SerialName("type")
    val type: MessageType = MessageType.TEXT,
    @SerialName("is_read")
    val isRead: Boolean = false,
    @SerialName("created_at")
    val createdAt: String
)

enum class MessageType {
    TEXT, IMAGE, DOCUMENT
}
@Serializable
data class Message(
    val id: String,
    @SerialName("chat_id")
    val chatId: String,
    @SerialName("sender_id")
    val senderId: String,
    @SerialName("receiver_id")
    val receiverId: String,
    val content: String,
    @SerialName("message_type")

    val type: String,
    @SerialName("is_read")
    val isRead: Boolean,
    @SerialName("created_at")
    val createdAt: String,
)

@Serializable
data class Notification(
    @SerialName("id")
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("title")
    val title: String,
    @SerialName("message")
    val message: String,
    @SerialName("type")
    val type: NotificationType = NotificationType.INFO,
    @SerialName("is_read")
    val isRead: Boolean = false,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("data")
    val data: Map<String, String>? = null
)

enum class NotificationType {
    INFO, SUCCESS, WARNING, ERROR, NEW_MESSAGE, NEW_AD
}

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

data class MessageUI(
    val id: String,
    val text: String,
    val time: String,
    val isSentByMe: Boolean,
    val isRead: Boolean = true,
    val type: MessageType = MessageType.TEXT
)

data class NotificationUI(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val isRead: Boolean = false,
    val type: NotificationType = NotificationType.INFO
)