package com.example.landmarketmobileapp.models.state

// UI States
data class AdvertisementListState(
    val advertisements: List<AdvertisementState> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasMore: Boolean = true
)