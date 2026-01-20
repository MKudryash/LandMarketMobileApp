package com.example.landmarketmobileapp.presentation.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.landmarketmobileapp.R
import com.example.landmarketmobileapp.models.state.AdvertisementState
import com.example.landmarketmobileapp.presentation.viewModels.AdvertisementViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AdvertisementCard(
    advertisement: AdvertisementState,
    viewModel: AdvertisementViewModel,
    onCardClick: () -> Unit
) {
    val formatter = NumberFormat.getNumberInstance(Locale.getDefault())
    var isFavorite by remember { mutableStateOf(advertisement.isFavorite) }

    Card(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Companion.White)
    ) {
        Column(modifier = Modifier.Companion.fillMaxWidth()) {
            // Изображение
            Box(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color.Companion.LightGray)
            ) {
                if (advertisement.imageUrls!!.isNotEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(advertisement.imageUrls.first())
                            .crossfade(true)
                            .build(),
                        contentDescription = "Изображение участка",
                        modifier = Modifier.Companion.fillMaxSize(),
                        contentScale = ContentScale.Companion.Crop
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
                            modifier = Modifier.Companion.size(64.dp)
                        )
                    }
                }

                // Бейдж "Новое"
                if (advertisement.datePosted.contains("сегодня") || advertisement.datePosted.contains(
                        "вчера"
                    )
                ) {
                    Box(
                        modifier = Modifier.Companion
                            .padding(8.dp)
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                            .background(Color.Companion.Red)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Companion.Center
                    ) {
                        Text(
                            text = "НОВОЕ",
                            fontSize = 10.sp,
                            color = Color.Companion.White,
                            fontWeight = FontWeight.Companion.Bold,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        )
                    }
                }

                // Кнопка избранного
                IconButton(
                    onClick = {
                        isFavorite = !isFavorite
                        viewModel.toggleFavorite(advertisement.id)
                    },
                    modifier = Modifier.Companion
                        .align(Alignment.Companion.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        if (isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "В избранное",
                        tint = if (isFavorite) Color.Companion.Red else Color.Companion.White
                    )
                }
            }

            // Контент карточки
            Column(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Заголовок и цена
                Row(
                    modifier = Modifier.Companion.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Companion.Top
                ) {
                    Text(
                        text = advertisement.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Companion.Bold,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        maxLines = 2,
                        overflow = TextOverflow.Companion.Ellipsis,
                        modifier = Modifier.Companion.weight(1f)
                    )

                    Spacer(modifier = Modifier.Companion.width(8.dp))

                    Text(
                        text = "${formatter.format(advertisement.price)} ₽",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Companion.Bold,
                        color = Color(0xFF6AA26C),
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )
                }

                Spacer(modifier = Modifier.Companion.height(8.dp))

                // Площадь и локация
                Row(
                    modifier = Modifier.Companion.fillMaxWidth(),
                    verticalAlignment = Alignment.Companion.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_area),
                        contentDescription = "Площадь",
                        tint = Color.Companion.Gray,
                        modifier = Modifier.Companion.size(16.dp)
                    )

                    Spacer(modifier = Modifier.Companion.width(4.dp))

                    Text(
                        text = "${advertisement.area} сот.",
                        fontSize = 14.sp,
                        color = Color.Companion.Gray,
                        fontFamily = FontFamily(Font(R.font.montserrat_medium))
                    )

                    Spacer(modifier = Modifier.Companion.width(16.dp))

                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Местоположение",
                        tint = Color.Companion.Gray,
                        modifier = Modifier.Companion.size(16.dp)
                    )

                    Spacer(modifier = Modifier.Companion.width(4.dp))

                    Text(
                        text = advertisement.location!!,
                        fontSize = 14.sp,
                        color = Color.Companion.Gray,
                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                        maxLines = 1,
                        overflow = TextOverflow.Companion.Ellipsis,
                        modifier = Modifier.Companion.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.Companion.height(8.dp))

                // Короткое описание
                Text(
                    text = advertisement.description.take(100) + if (advertisement.description.length > 100) "..." else "",
                    fontSize = 14.sp,
                    color = Color.Companion.DarkGray,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    maxLines = 2,
                    overflow = TextOverflow.Companion.Ellipsis
                )

                Spacer(modifier = Modifier.Companion.height(12.dp))

                // Коммуникации
                Row(
                    modifier = Modifier.Companion.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (advertisement.hasElectricity) {
                        CommunicationChip(
                            icon = Icons.Default.Share,
                            text = "Электричество"
                        )
                    }

                    if (advertisement.hasWater) {
                        CommunicationChip(
                            icon = Icons.Default.Share,
                            text = "Вода"
                        )
                    }

                    if (advertisement.hasRoad) {
                        CommunicationChip(
                            icon = Icons.Default.Share,
                            text = "Дорога"
                        )
                    }

                    if (advertisement.hasGas) {
                        CommunicationChip(
                            icon = Icons.Default.Share,
                            text = "Газ"
                        )
                    }

                    if (advertisement.hasInternet) {
                        CommunicationChip(
                            icon = Icons.Default.Share,
                            text = "Интернет"
                        )
                    }
                }

                Spacer(modifier = Modifier.Companion.height(12.dp))

                // Продавец и дата
                Row(
                    modifier = Modifier.Companion.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Companion.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.Companion.CenterVertically
                    ) {
                        // Аватар продавца
                        Box(
                            modifier = Modifier.Companion
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF6AA26C)),
                            contentAlignment = Alignment.Companion.Center
                        ) {
                            if (advertisement.sellerImage == "") {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = "Продавец",
                                    tint = Color.Companion.White,
                                    modifier = Modifier.Companion.size(14.dp)
                                )
                            } else {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(advertisement.sellerImage!!)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "Изображение",
                                    modifier = Modifier.Companion.size(120.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        Spacer(modifier = Modifier.Companion.width(8.dp))

                        Column {
                            Text(
                                text = advertisement.sellerName,
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.montserrat_medium))
                            )

                            Row(
                                verticalAlignment = Alignment.Companion.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = "Рейтинг",
                                    tint = Color(0xFFFFC107),
                                    modifier = Modifier.Companion.size(12.dp)
                                )

                                Spacer(modifier = Modifier.Companion.width(2.dp))

                                Text(
                                    text = advertisement.sellerRating.toString(),
                                    fontSize = 12.sp,
                                    color = Color.Companion.Gray,
                                    fontFamily = FontFamily(Font(R.font.montserrat_medium))
                                )
                            }
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.Companion.End
                    ) {
                        Text(
                            text = advertisement.datePosted,
                            fontSize = 12.sp,
                            color = Color.Companion.Gray,
                            fontFamily = FontFamily(Font(R.font.montserrat_regular))
                        )

                        Row(
                            verticalAlignment = Alignment.Companion.CenterVertically
                        ) {
                            Icon(
                                painterResource(R.drawable.eye_open),
                                contentDescription = "Просмотры",
                                tint = Color.Companion.Gray,
                                modifier = Modifier.Companion.size(12.dp)
                            )

                            Spacer(modifier = Modifier.Companion.width(2.dp))

                            Text(
                                text = advertisement.viewsCount.toString(),
                                fontSize = 12.sp,
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