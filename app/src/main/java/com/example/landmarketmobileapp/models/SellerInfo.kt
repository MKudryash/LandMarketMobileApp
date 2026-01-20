package com.example.landmarketmobileapp.models

import com.example.landmarketmobileapp.presentation.screens.Review

data class SellerInfo(
    val name: String = "",
    val rating: Float = 0f,
    val reviewsCount: Int = 0,
    val since: String = "",
    val reviews:List<Review>? = emptyList<Review>(),
    val image: String? = ""
)