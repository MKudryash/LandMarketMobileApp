package com.example.landmarketmobileapp.models

import io.github.jan.supabase.auth.OtpType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    @SerialName("full_name")
    val fullName:String,
    val email:String,
    val phone:String,
    @SerialName("avatar_url")
    val image:String?,
    val rating: Double?

)