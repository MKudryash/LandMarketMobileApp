package com.example.landmarketmobileapp.navigation

// Screen для добавления типа
sealed class Screen(val route: String, val screenType: ScreenType) {
    object Auth : Screen("auth", ScreenType.Auth)
    object SignUp : Screen("signup", ScreenType.Auth)

    object Main : Screen("main", ScreenType.WithBottomNav)
    object Profile : Screen("profile", ScreenType.WithBottomNav)
    object Messages : Screen("messages", ScreenType.WithBottomNav)
    object Ads : Screen("ads", ScreenType.WithBottomNav)
    object AdvertisementDetail : Screen("advertisementdetail/{id}", ScreenType.WithBottomNav){
        fun createRoute(id: String): String {
            return "advertisementdetail/${id}"
        }
    }
    object Chat : Screen("chat/{idChat}?userId={idUser}", ScreenType.WithBottomNav){
        fun createRoute(idChat: String,idUser:String): String {
            return "chat/$idChat?userId=$idUser"
        }
    }
    object Region : Screen("region/{id}", ScreenType.WithBottomNav){
        fun createRoute(id: String): String {
            return "region/${id}"
        }
    }
    object Village : Screen("village/{id}", ScreenType.WithBottomNav){
        fun createRoute(id: String): String {
            return "village/${id}"
        }
    }

}