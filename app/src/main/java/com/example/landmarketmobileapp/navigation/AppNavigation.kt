package com.example.landmarketmobileapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.landmarketmobileapp.screens.AdsScreen
import com.example.landmarketmobileapp.screens.AdvertisementDetailScreen
import com.example.landmarketmobileapp.screens.AuthScreen
import com.example.landmarketmobileapp.screens.ChatScreen
import com.example.landmarketmobileapp.screens.MainScreen
import com.example.landmarketmobileapp.screens.MessagesScreen
import com.example.landmarketmobileapp.screens.ProfileScreen
import com.example.landmarketmobileapp.screens.RegionScreen
import com.example.landmarketmobileapp.screens.SignUpScreen
import com.example.landmarketmobileapp.screens.VillageScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Auth.route // Или другой стартовый экран
    ) {


        composable(Screen.Auth.route) {
            AuthScreen(
                onNavigateToMain = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(0) // Очищаем весь стек до главного экрана
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUp.route) {
                    }
                }
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                onNavigateToAuth = { navController.navigate(Screen.Auth.route) },
                onNavigateToMain = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        // Основные экраны с панелью навигации
        composable(Screen.Main.route) {
            MainScreen(
                onNavigateToVillage = { it -> navController.navigate(Screen.Village.createRoute(it)) },
                onNavigateToRegion = { it -> navController.navigate(Screen.Region.createRoute(it)) }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                navigateLogOut = {navController.navigate(Screen.Auth.route){
                    popUpTo(0)
                } }
            )
        }
        composable(Screen.Messages.route) {
            MessagesScreen(
                onNavigationChat = {it->navController.navigate(Screen.Chat.createRoute(it))}
            )
        }
        composable(Screen.Ads.route) {
            AdsScreen(
                onNavigateDetailInform = { it ->
                    navController.navigate(
                        Screen.AdvertisementDetail.createRoute(
                            it
                        )
                    )
                }
            )
        }
        composable(
            Screen.Region.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            RegionScreen(id)
        }

        composable(
            Screen.Chat.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            ChatScreen(
                {
                    navController.navigate(Screen.Messages.route) {
                        popUpTo(0)
                    }
                },
                id
            )
        }

        composable(Screen.AdvertisementDetail.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
            AdvertisementDetailScreen(id,{
                navController.navigate(Screen.Ads.route){
                    popUpTo(0)
                }
            })

        }

        composable(Screen.Village.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
            VillageScreen(id)
        }
    }
}
