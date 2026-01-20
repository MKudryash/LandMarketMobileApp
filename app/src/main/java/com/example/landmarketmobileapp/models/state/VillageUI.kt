package com.example.landmarketmobileapp.models.state

data class VillageUI(
    val id: String,
    val name: String,
    val price: Int,
    val minPrice: Int,
    val location: String,
    val imageUrl: String = "",
    val description: String? = null,
    val distanceToCity: Int? = null,
    val slug: String? = null,
    val costPerHundred: Int,
    val costOfThePlot: Int,

)