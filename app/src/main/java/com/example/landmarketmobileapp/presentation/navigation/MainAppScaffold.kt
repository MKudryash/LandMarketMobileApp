package com.example.landmarketmobileapp.presentation.navigation


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlin.text.startsWith

@Composable
fun MainAppScaffold(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val shouldShowBottomNav = when {
        currentRoute?.startsWith("main") == true -> true
        currentRoute?.startsWith("ads") == true -> true
        currentRoute?.startsWith("messages") == true -> true
        currentRoute?.startsWith("profile") == true -> true
        else -> false
    }

    var selectedItem by remember { mutableStateOf<NavItem>(NavItem.Home) }


    LaunchedEffect(currentRoute) {
        selectedItem = when {
            currentRoute?.startsWith("main") == true -> NavItem.Home
            currentRoute?.startsWith("ads") == true -> NavItem.Ads
            currentRoute?.startsWith("messages") == true -> NavItem.Messages
            currentRoute?.startsWith("profile") == true -> NavItem.Profile // Исправлено
            else -> selectedItem
        }
    }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomNav) {
                BottomNavigationPanel(
                    selectedItem = selectedItem,
                    onItemSelected = { navItem ->
                        selectedItem = navItem
                        when (navItem) {
                            NavItem.Home -> {
                                navController.navigate(Screen.Main.route) {
                                    popUpTo(Screen.Main.route) { inclusive = true }
                                }
                            }
                            NavItem.Ads -> {
                                navController.navigate(Screen.Ads.route) {
                                    popUpTo(Screen.Ads.route) { inclusive = true }
                                }
                            }


                            NavItem.Messages -> {
                                navController.navigate(Screen.Messages.route) {
                                    popUpTo(Screen.Messages.route) { inclusive = true }
                                }
                            }
                            NavItem.Profile -> {
                                navController.navigate(Screen.Profile.route) {
                                    popUpTo(Screen.Profile.route) { inclusive = true }
                                }
                            }
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD9D9D9))
                .padding(paddingValues)
        ) {
            AppNavigation(navController)
        }
    }
}