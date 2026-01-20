package com.example.landmarketmobileapp.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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

