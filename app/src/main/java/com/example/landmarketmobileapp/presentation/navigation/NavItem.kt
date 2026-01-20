package com.example.landmarketmobileapp.presentation.navigation

sealed class NavItem(val title: String, val route: String) {
    object Home : NavItem("Главная", Screen.Main.route)
    object Ads : NavItem("Объявления", Screen.Ads.route)
    object Messages : NavItem("Сообщения", Screen.Messages.route)
    object Profile : NavItem("Профиль", Screen.Profile.route)
}