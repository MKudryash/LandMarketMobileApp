package com.example.landmarketmobileapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.landmarketmobileapp.R
import com.example.landmarketmobileapp.models.state.AdvertisementState
import java.text.NumberFormat
import java.util.Locale

@Composable
fun MainInfoSection(advertisement: AdvertisementState) {
    val formatter = NumberFormat.getNumberInstance(Locale.getDefault())

    Column(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .background(Color.Companion.White)
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
                fontSize = 22.sp,
                fontWeight = FontWeight.Companion.Bold,
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                modifier = Modifier.Companion.weight(1f)
            )

            Spacer(modifier = Modifier.Companion.width(16.dp))

            Column(horizontalAlignment = Alignment.Companion.End) {
                Text(
                    text = "${formatter.format(advertisement.price)} ₽",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Companion.Bold,
                    color = Color(0xFF6AA26C),
                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                )
                if (advertisement.area > 0) {
                    Text(
                        text = "${formatter.format(advertisement.price / advertisement.area)} ₽/сотка",
                        fontSize = 14.sp,
                        color = Color.Companion.Gray,
                        fontFamily = FontFamily(Font(R.font.montserrat_regular))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.Companion.height(12.dp))

        // Основные характеристики
        Row(
            modifier = Modifier.Companion.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            CharacteristicItem(
                icon = Icons.Default.ShoppingCart,
                value = "${advertisement.area} соток",
                label = "Площадь"
            )

            CharacteristicItem(
                icon = Icons.Default.LocationOn,
                value = "${advertisement.distanceToCity} км",
                label = "До города"
            )

            CharacteristicItem(
                icon = Icons.Default.DateRange,
                value = advertisement.datePosted,
                label = "Опубликовано"
            )
        }

        Spacer(modifier = Modifier.Companion.height(12.dp))

        // Локация
        Row(
            modifier = Modifier.Companion.fillMaxWidth(),
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {
            Icon(
                Icons.Default.Place,
                contentDescription = "Местоположение",
                tint = Color.Companion.Gray,
                modifier = Modifier.Companion.size(20.dp)
            )

            Spacer(modifier = Modifier.Companion.width(8.dp))

            Text(
                text = advertisement.location!!,
                fontSize = 14.sp,
                color = Color.Companion.DarkGray,
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                modifier = Modifier.Companion.weight(1f)
            )
        }

        // Кадастровый номер
        if (advertisement.cadastralNumber.isNotEmpty()) {
            Spacer(modifier = Modifier.Companion.height(8.dp))
            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                verticalAlignment = Alignment.Companion.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.corp_ads),
                    contentDescription = "Кадастровый номер",
                    tint = Color.Companion.Gray,
                    modifier = Modifier.Companion.size(20.dp)
                )

                Spacer(modifier = Modifier.Companion.width(8.dp))

                Text(
                    text = "Кадастр: ${advertisement.cadastralNumber}",
                    fontSize = 14.sp,
                    color = Color.Companion.DarkGray,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular))
                )
            }
        }

        // Просмотры
        Spacer(modifier = Modifier.Companion.height(8.dp))
        Row(
            modifier = Modifier.Companion.fillMaxWidth(),
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {
            Icon(
                painterResource(R.drawable.eye_open),
                contentDescription = "Просмотры",
                tint = Color.Companion.Gray,
                modifier = Modifier.Companion.size(20.dp)
            )

            Spacer(modifier = Modifier.Companion.width(8.dp))

            Text(
                text = "${advertisement.viewsCount} просмотров",
                fontSize = 14.sp,
                color = Color.Companion.Gray,
                fontFamily = FontFamily(Font(R.font.montserrat_regular))
            )
        }
    }
}