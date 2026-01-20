package com.example.landmarketmobileapp.models

data class Category(
    val id: String,
    val name: String,
    val iconResId: Int,
    val count: Int = 0
)