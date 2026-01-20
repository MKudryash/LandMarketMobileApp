package com.example.landmarketmobileapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.landmarketmobileapp.presentation.navigation.MainAppScaffold
import com.example.landmarketmobileapp.presentation.theme.LandMarketMobileAppTheme
import com.yandex.mapkit.MapKitFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MapKitFactory.setApiKey("25fac3a0-fc49-4545-92b1-d1d64d54dac5")
            MapKitFactory.initialize(this)
            LandMarketMobileAppTheme {
                val navController = rememberNavController()

                MainAppScaffold(navController = navController)
            }
        }
    }
}
