package com.example.landmarketmobileapp.navigation

// Типы экранов для управления навигацией
sealed class ScreenType {
    object Auth : ScreenType()           // Экран без панели (аутентификация)
    object WithoutBottomNav : ScreenType() // Экран без панели (например, Train, TaskInfo)
    object WithBottomNav : ScreenType()    // Экран с панелью (основные экраны)
}

// Расширим Screen для добавления типа
sealed class Screen(val route: String, val screenType: ScreenType) {
    object Auth : Screen("auth", ScreenType.Auth)
    object SignUp : Screen("signup", ScreenType.Auth)

    object Main : Screen("main", ScreenType.WithBottomNav)
    object Profile : Screen("profile", ScreenType.WithBottomNav)
    object Messages : Screen("messages", ScreenType.WithBottomNav)
    object Ads : Screen("ads", ScreenType.WithBottomNav)
    object Region : Screen("region", ScreenType.WithBottomNav){
        fun createRoute(id: String): String {
            return "region/${id}"
        }
    }
    object Village : Screen("village", ScreenType.WithBottomNav){
        fun createRoute(id: String): String {
            return "village/${id}"
        }
    }

}