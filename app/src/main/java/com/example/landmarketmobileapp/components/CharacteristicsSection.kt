package com.example.landmarketmobileapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.landmarketmobileapp.R
import com.example.landmarketmobileapp.viewModels.AdvertisementState

@Composable
fun CharacteristicsSection(advertisement: AdvertisementState) {
    Column(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .background(Color.Companion.White)
            .padding(16.dp)
            .padding(top = 12.dp)
    ) {
        Text(
            text = "Характеристики",
            fontSize = 18.sp,
            fontWeight = FontWeight.Companion.Bold,
            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
            modifier = Modifier.Companion.padding(bottom = 12.dp)
        )

        // Сетка характеристик
        val characteristics = listOf(
            Pair("Назначение", advertisement.purpose),
            Pair("Тип почвы", advertisement.soilType),
            Pair("Рельеф", advertisement.relief),
            Pair("Электричество", if (advertisement.hasElectricity) "Есть" else "Нет"),
            Pair("Водоснабжение", if (advertisement.hasWater) "Есть" else "Нет"),
            Pair("Газ", if (advertisement.hasGas) "Есть" else "Нет"),
            Pair("Дорога", if (advertisement.hasRoad) "Есть" else "Нет"),
            Pair("Интернет", if (advertisement.hasInternet) "Есть" else "Нет")
        )

        Column {
            characteristics.chunked(2).forEach { pair ->
                Row(
                    modifier = Modifier.Companion.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    pair.forEach { (label, value) ->
                        Column(
                            modifier = Modifier.Companion.weight(1f)
                        ) {
                            Text(
                                text = label,
                                fontSize = 14.sp,
                                color = Color.Companion.Gray,
                                fontFamily = FontFamily(Font(R.font.montserrat_regular))
                            )
                            Text(
                                text = value,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Companion.Medium,
                                fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                                modifier = Modifier.Companion.padding(top = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.Companion.height(12.dp))
            }
        }
    }
}