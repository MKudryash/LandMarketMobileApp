package com.example.landmarketmobileapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.landmarketmobileapp.R
import com.example.landmarketmobileapp.components.CardRegion
import com.example.landmarketmobileapp.components.CardVillage
import com.example.landmarketmobileapp.components.MainHeader
import com.example.landmarketmobileapp.components.Region
import com.example.landmarketmobileapp.components.SearchBarWithSettings
import com.example.landmarketmobileapp.components.SecondHeader
import com.example.landmarketmobileapp.components.SecondMainHeader
import com.example.landmarketmobileapp.components.ThirdHeader
import okhttp3.internal.http2.Header
import java.text.NumberFormat
import java.util.Locale
import kotlin.collections.chunked

@Composable
fun MainScreen(onNavigateToVillage: (String) -> Unit, onNavigateToRegion: (String) -> Unit) {
    var searchText by remember { mutableStateOf("") }
    var showFilterDialog by remember { mutableStateOf(false) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD9D9D9))
            .padding(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {

        item { Spacer(Modifier.height(10.dp)) }

        item { MainHeader("О НАС") }

        item { Spacer(Modifier.height(10.dp)) }

        // О нас
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "GOOD-ZEM — лидирующая онлайн-платформа для продажи земельных участков",
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(5.dp)
                )
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(5.dp)
                ) {
                    ItemAboutUs(10000, "предложений")
                    ItemAboutUs(7600, "продавцов")
                    ItemAboutUs(25000, "сделок")
                }
            }
        }

        item { Spacer(Modifier.height(10.dp)) }

        item { SecondHeader("Поселки в Нижегородской области") }

        item { Spacer(Modifier.height(10.dp)) }

        // Горизонтальный список поселков
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    CardVillage(
                        "", "Бахтаево PARK", 600000, 150000, "Егорьевское ш. 45 км.",
                    )
                }
                item {
                    CardVillage(
                        "", "Сосновый Бор", 450000, 120000, "Новорижское ш. 25 км.",
                    )
                }
                item {
                    CardVillage(
                        "", "Речной Порт", 800000, 200000, "Ленинградское ш. 35 км.",
                    )
                }
                item {
                    CardVillage(
                        "", "Зеленые Холмы", 550000, 180000, "Калужское ш. 40 км.",
                    )
                }
            }
        }

        item { Spacer(Modifier.height(10.dp)) }

        item { ThirdHeader("Смотреть все поселки") }

        item { Spacer(Modifier.height(20.dp)) }

        item {
            Text(
                "GOOD-ZEM — лидирующая онлайн-платформа для продажи земельных участков*",
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 22.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
        }

        item { Spacer(Modifier.height(20.dp)) }

        item { SecondMainHeader("Доступные участки") }

        item { Spacer(Modifier.height(10.dp)) }

        // Сетка регионов
        val regions = listOf(
            Region("", "Бахтаево PARK", 150000, "Егорьевское ш. 45 км."),
            Region("", "Сосновый Бор", 120000, "Новорижское ш. 25 км."),
            Region("", "Речной Порт", 200000, "Ленинградское ш. 35 км."),
            Region("", "Зеленые Холмы", 180000, "Калужское ш. 40 км."),
            Region("", "Лесная Сказка", 160000, "Минское ш. 30 км."),
            Region("", "Солнечная Долина", 140000, "Киевское ш. 50 км.")
        )
        val groupedAchievements = regions.chunked(2)
        items(regions.chunked(2)) { pair ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                pair.forEach { region ->
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        CardRegion(region)
                    }
                }

                // Если в паре только один элемент, добавляем пустой Box для баланса
                if (pair.size == 1) {
                    Box(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun ItemAboutUs(
    number: Int,
    text: String,
) {
    val formatter = NumberFormat.getNumberInstance(Locale.getDefault())
    Row {
        Icon(
            painter = painterResource(R.drawable.more),
            tint = Color(0xFF6AA26C),
            contentDescription = "",
            modifier = Modifier.size(25.dp)
        )
        Column {
            Text(
                formatter.format(number),
                color = Color(0xFF6AA26C),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )
            Text(
                text,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_regular))
            )
        }
    }
}

@Composable
fun MainScreenPreview() {
    MainScreen({}, {})
}