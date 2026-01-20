package com.example.landmarketmobileapp.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("advertisement_images")
data class AdvertisementImages(
    val id: String,
    @SerialName("created_at")
    val createAt: String,
    @SerialName("id_ad")
    val idAd: String,
    val url: String,

)