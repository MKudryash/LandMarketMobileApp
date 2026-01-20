package com.example.landmarketmobileapp.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Favorites(
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("advertisement_id")
    val advertisementId: String,
    @SerialName("created_at")
    val createdAt: String

)