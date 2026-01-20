package com.example.landmarketmobileapp.presentation.navigation

// Типы экранов для управления навигацией
sealed class ScreenType {
    object Auth : ScreenType()           // Экран без панели (аутентификация)
    object WithoutBottomNav : ScreenType() // Экран без панели (например, Train, TaskInfo)
    object WithBottomNav : ScreenType()    // Экран с панелью (основные экраны)
}