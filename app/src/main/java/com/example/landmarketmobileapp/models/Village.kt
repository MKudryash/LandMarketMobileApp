package com.example.landmarketmobileapp.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Village(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("slug") val slug: String? = null,
    @SerialName("region_id") val regionId: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("distance_to_city") val distanceToCity: Int? = null,
    @SerialName("infrastructure") val infrastructure: Map<String, Boolean>? = null,
    @SerialName("is_active") val isActive: Boolean = true,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("image_url") val image: String?,
    @SerialName("costOfThePlot") val costOfThePlot: Int? = null,
    @SerialName("costPerHundred") val costPerHundred: Int? = null,


    )