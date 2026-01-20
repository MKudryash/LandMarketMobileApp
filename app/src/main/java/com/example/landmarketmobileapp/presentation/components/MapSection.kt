package com.example.landmarketmobileapp.presentation.components

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.landmarketmobileapp.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import ru.sulgik.mapkit.compose.YandexMap
import ru.sulgik.mapkit.compose.bindToLifecycleOwner
import ru.sulgik.mapkit.compose.rememberAndInitializeMapKit
import ru.sulgik.mapkit.compose.rememberCameraPositionState
import ru.sulgik.mapkit.geometry.Point
import ru.sulgik.mapkit.map.CameraPosition

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapSection(
    onMapClick: () -> Unit,
    center_latitude: Double,
    center_longitude: Double,
    location: String
) {
    val context = LocalContext.current
    var showLocationPermissionDialog by remember { mutableStateOf(false) }

    // Состояние для разрешений
    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    // Запрос разрешений при первом рендере
    LaunchedEffect(Unit) {
        if (locationPermissionState.status != PermissionStatus.Granted) {
            showLocationPermissionDialog = true
        }
    }

    Column(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .background(Color.Companion.White)
            .padding(16.dp)
            .padding(top = 12.dp)
    ) {
        Row(
            modifier = Modifier.Companion.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {
            Text(
                text = "Расположение",
                fontSize = 18.sp,
                fontWeight = FontWeight.Companion.Bold,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )

            TextButton(onClick = onMapClick) {
                Text("Открыть карту")
            }
        }

        Text(
            text = location,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
            color = Color.Companion.DarkGray,
            modifier = Modifier.Companion.padding(top = 4.dp, bottom = 12.dp)
        )

        // Проверяем разрешения перед отображением карты
        if (locationPermissionState.status == PermissionStatus.Granted) {
            // Разрешения есть - показываем карту
            rememberAndInitializeMapKit().bindToLifecycleOwner()
            val startPosition = CameraPosition(
                Point(center_latitude, center_longitude),
                15.0f,
                0.0f,
                0.0f
            )
            val cameraPositionState = rememberCameraPositionState { position = startPosition }

            Box(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(onClick = onMapClick)
            ) {
                YandexMap(
                    cameraPositionState = cameraPositionState,
                    modifier = Modifier.Companion.fillMaxSize()
                )

                // Затемнение и текст поверх карты для кликабельности
                Box(
                    modifier = Modifier.Companion
                        .fillMaxSize()
                        .background(Color.Companion.Black.copy(alpha = 0.2f))
                )

                Column(
                    modifier = Modifier.Companion.align(Alignment.Companion.Center),
                    horizontalAlignment = Alignment.Companion.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowUp,
                        contentDescription = "Увеличить",
                        tint = Color.Companion.White,
                        modifier = Modifier.Companion
                            .size(48.dp)
                            .background(
                                Color(0xFF6AA26C),
                                CircleShape
                            )
                            .padding(12.dp)
                    )

                    Spacer(modifier = Modifier.Companion.height(8.dp))

                    Text(
                        text = "Нажмите для детального просмотра",
                        fontSize = 16.sp,
                        color = Color.Companion.White,
                        fontFamily = FontFamily(Font(R.font.montserrat_medium))
                    )
                }
            }
        } else {
            // Разрешений нет - показываем кнопку для запроса
            Box(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                    .background(Color(0xFF6AA26C).copy(alpha = 0.1f))
                    .clickable {
                        showLocationPermissionDialog = true
                    },
                contentAlignment = Alignment.Companion.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.Companion.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Нет разрешения",
                        tint = Color(0xFF6AA26C),
                        modifier = Modifier.Companion.size(48.dp)
                    )

                    Spacer(modifier = Modifier.Companion.height(8.dp))

                    Text(
                        text = "Разрешите доступ к местоположению",
                        fontSize = 16.sp,
                        color = Color(0xFF6AA26C),
                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                        textAlign = TextAlign.Companion.Center,
                        modifier = Modifier.Companion.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.Companion.height(8.dp))

                    Button(
                        onClick = {
                            showLocationPermissionDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6AA26C)
                        )
                    ) {
                        Text("Запросить разрешение")
                    }
                }
            }
        }
    }

    // Диалог запроса разрешений
    if (showLocationPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showLocationPermissionDialog = false },
            title = {
                Text(
                    text = "Доступ к местоположению",
                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                )
            },
            text = {
                Text(
                    text = "Для отображения карты и вашего местоположения необходимо предоставить доступ к геолокации",
                    fontFamily = FontFamily(Font(R.font.montserrat_regular))
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        locationPermissionState.launchPermissionRequest()
                        showLocationPermissionDialog = false
                    }
                ) {
                    Text("Разрешить")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLocationPermissionDialog = false }
                ) {
                    Text("Отмена")
                }
            }
        )
    }
}