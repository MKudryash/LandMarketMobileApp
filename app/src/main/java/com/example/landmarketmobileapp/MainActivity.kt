package com.example.landmarketmobileapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.landmarketmobileapp.navigation.MainAppScaffold
import com.example.landmarketmobileapp.ui.theme.LandMarketMobileAppTheme
import com.example.landmarketmobileapp.viewModels.AuthViewModel
import com.yandex.mapkit.MapKitFactory
import kotlinx.coroutines.launch

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
