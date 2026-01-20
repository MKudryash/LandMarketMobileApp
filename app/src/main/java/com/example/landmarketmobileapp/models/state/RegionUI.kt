package com.example.landmarketmobileapp.models.state

data class RegionUI(
    val id: String,
    val name: String,
    val address: String,
    val price: Int,
    val costPerHundred: Int,
    val location: String,
    val imageUrl: String = "",
    val description: String? = null,
    val slug: String? = null
)