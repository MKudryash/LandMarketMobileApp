package com.example.landmarketmobileapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.landmarketmobileapp.R

@Composable
fun GalleryDialog(
    imageUrls: List<String>,
    initialIndex: Int,
    onDismiss: () -> Unit,
) {
    var currentIndex by remember { mutableStateOf(initialIndex) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(modifier = Modifier.Companion.fillMaxSize()) {
                if (imageUrls.isNotEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrls[currentIndex])
                            .crossfade(true)
                            .build(),
                        contentDescription = "Изображение",
                        modifier = Modifier.Companion.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Box(
                        modifier = Modifier.Companion.fillMaxSize(),
                        contentAlignment = Alignment.Companion.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_app),
                            contentDescription = "Нет изображения",
                            tint = Color(0xFF6AA26C),
                            modifier = Modifier.Companion.size(80.dp)
                        )
                    }
                }

                // Кнопка закрытия
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.Companion
                        .padding(16.dp)
                        .align(Alignment.Companion.TopEnd)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Закрыть",
                        tint = Color.Companion.White
                    )
                }

                // Навигация по изображениям
                if (imageUrls.size > 1) {
                    Row(
                        modifier = Modifier.Companion
                            .fillMaxWidth()
                            .align(Alignment.Companion.BottomCenter)
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                if (currentIndex > 0) currentIndex--
                            },
                            enabled = currentIndex > 0,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Companion.Black.copy(alpha = 0.5f)
                            ),
                            shape = CircleShape
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                        }

                        Spacer(modifier = Modifier.Companion.width(16.dp))

                        Text(
                            text = "${currentIndex + 1}/${imageUrls.size}",
                            color = Color.Companion.White,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat_medium))
                        )

                        Spacer(modifier = Modifier.Companion.width(16.dp))

                        Button(
                            onClick = {
                                if (currentIndex < imageUrls.size - 1) currentIndex++
                            },
                            enabled = currentIndex < imageUrls.size - 1,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Companion.Black.copy(alpha = 0.5f)
                            ),
                            shape = CircleShape
                        ) {
                            Icon(Icons.Default.ArrowForward, contentDescription = "Вперед")
                        }
                    }
                }
            }
        }
    }
}