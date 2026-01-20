package com.example.landmarketmobileapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.landmarketmobileapp.R

@Composable
fun GallerySection(
    imageUrls: List<String>,
    selectedIndex: Int,
    onImageSelected: (Int) -> Unit,
    onGalleryClick: () -> Unit,
) {
    Column(modifier = Modifier.Companion.fillMaxWidth()) {
        // Главное изображение
        Box(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .height(300.dp)
                .background(Color.Companion.LightGray)
                .clickable(onClick = onGalleryClick)
        ) {
            if (imageUrls.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrls.getOrNull(selectedIndex))
                        .crossfade(true)
                        .build(),
                    contentDescription = "Изображение участка",
                    modifier = Modifier.Companion.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier.Companion
                        .fillMaxSize()
                        .background(Color(0xFF6AA26C).copy(alpha = 0.1f)),
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

            // Счетчик изображений
            if (imageUrls.size > 1) {
                Box(
                    modifier = Modifier.Companion
                        .padding(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Companion.Black.copy(alpha = 0.5f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .align(Alignment.Companion.TopEnd)
                ) {
                    Text(
                        text = "${selectedIndex + 1}/${imageUrls.size}",
                        color = Color.Companion.White,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat_medium))
                    )
                }
            }

            // Кнопка галереи
            IconButton(
                onClick = onGalleryClick,
                modifier = Modifier.Companion
                    .padding(16.dp)
                    .clip(CircleShape)
                    .background(Color.Companion.Black.copy(alpha = 0.5f))
                    .align(Alignment.Companion.BottomEnd)
            ) {
                Icon(
                    painterResource(R.drawable.camera),
                    contentDescription = "Галерея",
                    tint = Color.Companion.White
                )
            }
        }

        // Миниатюры
        if (imageUrls.size > 1) {
            LazyRow(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(imageUrls.size) { index ->
                    Box(
                        modifier = Modifier.Companion
                            .size(60.dp)
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                            .background(
                                if (index == selectedIndex) Color(0xFF6AA26C)
                                else Color.Companion.Gray.copy(alpha = 0.3f)
                            )
                            .border(
                                width = if (index == selectedIndex) 2.dp else 0.dp,
                                color = Color(0xFF6AA26C),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                            )
                            .clickable { onImageSelected(index) }
                    ) {
                        if (imageUrls[index].isNotEmpty()) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(imageUrls[index])
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Миниатюра",
                                modifier = Modifier.Companion.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier.Companion.fillMaxSize(),
                                contentAlignment = Alignment.Companion.Center
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    color = if (index == selectedIndex) Color.Companion.White else Color.Companion.Black,
                                    fontFamily = FontFamily(Font(R.font.montserrat_medium))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}