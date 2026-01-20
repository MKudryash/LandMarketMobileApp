package com.example.landmarketmobileapp.components

import android.Manifest
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.landmarketmobileapp.R
import com.example.landmarketmobileapp.viewModels.CoordinatesState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import ru.sulgik.mapkit.compose.Placemark
import ru.sulgik.mapkit.compose.YandexMap
import ru.sulgik.mapkit.compose.rememberCameraPositionState
import ru.sulgik.mapkit.compose.rememberPlacemarkState
import ru.sulgik.mapkit.geometry.Point
import ru.sulgik.mapkit.map.CameraPosition
import ru.sulgik.mapkit.map.ImageProvider
import ru.sulgik.mapkit.map.fromBitmap

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapDialog(
    location: String,
    center_longitude:Double,
    center_latitude: Double,
    coordinates: CoordinatesState?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier.Companion
) {
    val context = LocalContext.current

    // Проверка разрешений на местоположение
    val fineLocationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    val coarseLocationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val hasLocationPermission = fineLocationPermissionState.status == PermissionStatus.Granted ||
            coarseLocationPermissionState.status == PermissionStatus.Granted

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = true
        )
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.Companion.fillMaxSize()
            ) {
                // Заголовок
                Row(
                    modifier = Modifier.Companion
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Companion.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.Companion.weight(1f)
                    ) {
                        Text(
                            text = "Расположение участка",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Companion.Bold,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                            color = Color.Companion.Black
                        )

                        Spacer(modifier = Modifier.Companion.height(4.dp))

                        Text(
                            text = location,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                            color = Color.Companion.Gray,
                            maxLines = 2,
                            overflow = TextOverflow.Companion.Ellipsis
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.Companion
                            .size(36.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Закрыть",
                            tint = Color.Companion.Gray
                        )
                    }
                }

                // Разделитель
                Divider(
                    color = Color.Companion.LightGray.copy(alpha = 0.3f),
                    thickness = 1.dp
                )

                // Контент карты
                Box(
                    modifier = Modifier.Companion
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    Log.d("COOR", "${center_longitude}, ${center_latitude}")
                    if (true) {
                        if (hasLocationPermission) {
                            val startPosition = CameraPosition(
                                Point(center_latitude, center_longitude),
                                15.0f,
                                0.0f,
                                0.0f
                            )
                            val cameraPositionState =
                                rememberCameraPositionState { position = startPosition }
                            Box(
                                modifier = Modifier.Companion
                                    .fillMaxWidth()
                                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                            ) {
                                YandexMap(
                                    cameraPositionState = cameraPositionState,
                                    modifier = Modifier.Companion.fillMaxSize()
                                ) {
                                    val placemarkGeometry = Point(center_latitude, center_longitude)

                                    // Создаем простой Bitmap программно
                                    val bitmap =
                                        Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888).apply {
                                            // Прозрачный фон
                                            eraseColor(android.graphics.Color.TRANSPARENT)

                                            val canvas = Canvas(this)
                                            val paint = Paint().apply {
                                                color = android.graphics.Color.RED
                                                style = Paint.Style.FILL
                                                isAntiAlias = true
                                            }

                                            // Рисуем булавку локации
                                            canvas.drawCircle(32f, 20f, 10f, paint)  // верхний круг

                                            // Нижний треугольник (заостренный)
                                            val path = Path()
                                            path.moveTo(22f, 20f)
                                            path.lineTo(32f, 55f)
                                            path.lineTo(42f, 20f)
                                            path.close()
                                            canvas.drawPath(path, paint)
                                        }

                                    val imageProvider = ImageProvider.Companion.fromBitmap(bitmap)

                                    Placemark(
                                        state = rememberPlacemarkState(placemarkGeometry),
                                        icon = imageProvider,
                                    )
                                }
                            }


                        } else {
                            // Без разрешений - показываем статичную карту с кнопкой запроса
                            Box(
                                modifier = Modifier.Companion
                                    .fillMaxSize()
                                    .background(Color(0xFF6AA26C).copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Companion.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.Companion.CenterHorizontally,
                                    modifier = Modifier.Companion.padding(24.dp)
                                ) {
                                    Icon(
                                        Icons.Default.LocationOn,
                                        contentDescription = "Карта",
                                        tint = Color(0xFF6AA26C),
                                        modifier = Modifier.Companion.size(64.dp)
                                    )

                                    Spacer(modifier = Modifier.Companion.height(16.dp))

                                    Text(
                                        text = location,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                                        textAlign = TextAlign.Companion.Center,
                                        color = Color.Companion.Black
                                    )

                                    Spacer(modifier = Modifier.Companion.height(8.dp))

                                    Text(
                                        text = "Координаты: ${center_latitude}, ${center_longitude}",
                                        fontSize = 14.sp,
                                        color = Color.Companion.Gray,
                                        fontFamily = FontFamily(Font(R.font.montserrat_regular))
                                    )

                                    Spacer(modifier = Modifier.Companion.height(24.dp))

                                    Text(
                                        text = "Для отображения интерактивной карты требуется доступ к местоположению",
                                        fontSize = 14.sp,
                                        color = Color.Companion.Gray,
                                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                                        textAlign = TextAlign.Companion.Center
                                    )

                                    Spacer(modifier = Modifier.Companion.height(16.dp))

                                }
                            }
                        }
                    } else {
                        // Координаты не указаны
                        Box(
                            modifier = Modifier.Companion
                                .fillMaxSize()
                                .background(Color(0xFF6AA26C).copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Companion.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.Companion.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.LocationOn,
                                    contentDescription = "Координаты не указаны",
                                    tint = Color(0xFF6AA26C),
                                    modifier = Modifier.Companion.size(64.dp)
                                )

                                Spacer(modifier = Modifier.Companion.height(16.dp))

                                Text(
                                    text = location,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                                    textAlign = TextAlign.Companion.Center,
                                    modifier = Modifier.Companion.padding(horizontal = 16.dp)
                                )

                                Spacer(modifier = Modifier.Companion.height(8.dp))

                                Text(
                                    text = "Координаты не указаны",
                                    fontSize = 14.sp,
                                    color = Color.Companion.Gray,
                                    fontFamily = FontFamily(Font(R.font.montserrat_regular))
                                )
                            }
                        }
                    }
                }


            }
        }
    }
}