package com.example.landmarketmobileapp.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Model для базы данных
@Serializable
data class Advertisement(
    val id: String? = null,
    @SerialName("user_id")
    val userId: String? = null,
    val title: String? = null,
    val slug: String? = null,
    val description: String? = null,
    @SerialName("category_id")
    val categoryId: String? = null,
    val villageId: String? = null,
    val regionId: String? = null,
    val price: Double? = null,
    val area: Double? = null,
    val pricePerHundred: Double? = null,
    val hasElectricity: Boolean? = null,
    val hasWater: Boolean? = null,
    val hasGas: Boolean? = null,
    val hasRoad: Boolean? = null,
    val hasInternet: Boolean? = null,
    val soilType: String? = null,
    val reliefType: String? = null,
    val cadastralNumber: String? = null,
    val purpose: String? = null,
    val documents: List<String>? = null,
    val status: String? = null,
    @SerialName("views_count")
    val viewsCount: Int? = null,
    val isPremium: Boolean? = null,
    val isFeatured: Boolean? = null,
    val featuredUntil: String? = null,
    val contactPhone: String? = null,
    val contactEmail: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val publishedAt: String? = null,
    val expiresAt: String? = null,
    @SerialName("image_url")
    val imageUrl: String? = null,
    val center_latitude: Double? = null,
    val center_longitude: Double? =null,
)