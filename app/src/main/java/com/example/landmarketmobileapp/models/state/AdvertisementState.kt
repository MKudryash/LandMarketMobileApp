package com.example.landmarketmobileapp.models.state

data class AdvertisementState(
    val id: String,
    val title: String,
    val description: String,
    val price: Int,
    val area: Int, // площадь в сотках
    val location: String?,
    val coordinates: CoordinatesState? = null,
    val imageUrls: List<String>? = emptyList(), // несколько фото
    val isFavorite: Boolean = false,
    val datePosted: String,
    val sellerName: String,
    val sellerRating: Float,
    val sellerReviewsCount: Int = 0,
    val sellerSince: String = "",
    val hasElectricity: Boolean = false,
    val hasWater: Boolean = false,
    val hasRoad: Boolean = false,
    val hasGas: Boolean = false,
    val hasInternet: Boolean = false,
    val soilType: String = "Чернозем",
    val relief: String = "Ровный",
    val distanceToCity: Int = 10, // км до города
    val cadastralNumber: String = "",
    val purpose: String = "ИЖС", // назначение земли
    val documents: List<String> = emptyList(),
    var viewsCount: Int = 0,
    val phoneNumber: String = "",
    val features: List<String> = emptyList(), // особенности
    val additionalInfo: String = "",
    val imageUrl: String? = null,
    val reviews: List<ReviewState>? = emptyList(),
    val sellerImage: String? = "",
    val center_latitude: Double? =null,
    val center_longitude: Double? =null,
)