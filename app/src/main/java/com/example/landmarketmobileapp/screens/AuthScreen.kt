package com.example.landmarketmobileapp.screens

import androidx.compose.runtime.Composable

@Composable
fun AuthScreen(onNavigateToMain: () -> Unit, onNavigateToSignUp: () -> Unit) {
}
@Composable
fun SignUpScreen(onNavigateToAuth: () -> Unit, onNavigateToMain: () -> Unit) {

}
@Composable
fun MainScreen(onNavigateToVillage: (String) -> Unit, onNavigateToRegion: (String) -> Unit) {

}

@Composable
fun ProfileScreen() {

}

@Composable
fun MessagesScreen() {

}
@Composable
fun AdsScreen() {

}

@Composable
fun RegionScreen(id: String) {

}
@Composable
fun VillageScreen(id: String) {

}