package com.example.landmarketmobileapp.models.state

data class MainScreenState(
    val villages: List<VillageUI> = emptyList(),
    val regions: List<RegionUI> = emptyList(),
    val stats: Map<String, Int> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null
)