package com.example.landmarketmobileapp.models.state

data class ReviewState(
    val id: String,
    val reviewer_id: String,
    val seller_id: String,
    val advertisement_id: String,
    val rating: Float,
    val title: String,
    val comment: String,
    val helpful_count: Int = 0,
    val is_verified_purchase: Boolean,
    val username: String,
    val createdAt: String,
    val image: String? = ""
)