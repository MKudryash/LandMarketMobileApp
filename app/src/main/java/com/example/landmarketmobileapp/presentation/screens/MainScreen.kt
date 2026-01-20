package com.example.landmarketmobileapp.presentation.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.landmarketmobileapp.R
import com.example.landmarketmobileapp.presentation.components.CardRegion
import com.example.landmarketmobileapp.presentation.components.CardVillage
import com.example.landmarketmobileapp.presentation.components.ItemAboutUs
import com.example.landmarketmobileapp.presentation.components.MainHeader
import com.example.landmarketmobileapp.presentation.components.SecondHeader
import com.example.landmarketmobileapp.presentation.components.SecondMainHeader
import com.example.landmarketmobileapp.presentation.components.ThirdHeader
import com.example.landmarketmobileapp.presentation.viewModels.MainViewModel
import kotlin.collections.chunked



@Composable
fun MainScreen(
    onNavigateToVillage: (String) -> Unit,
    onNavigateToRegion: (String) -> Unit,
    onNavigateToAllVillages: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    viewModel: MainViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val searchText by viewModel.searchText.collectAsState()

    // Показываем индикатор загрузки
    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD9D9D9)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF6AA26C))
        }
        return
    }

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
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "LANDMARKET — лидирующая онлайн-платформа для продажи земельных участков",
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    lineHeight = 24.sp,
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(5.dp)
                )
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(5.dp)
                ) {
                    state.stats.entries.forEach { (key, value) ->
                        ItemAboutUs(value, key)
                    }
                }
            }
        }

        item { Spacer(Modifier.height(10.dp)) }

        item { SecondHeader("Популярные поселки") }

        item { Spacer(Modifier.height(10.dp)) }

        // Горизонтальный список поселков
        item {
            if (state.villages.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Поселки не найдены",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            } else {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(state.villages) { index,village ->
                        CardVillage(
                            village.imageUrl,
                            title = village.name,
                            costOfThePlot = village.price,
                            costPerHundred = village.minPrice,
                            address = village.location,
                        )
                    }
                }
            }
        }

        item { Spacer(Modifier.height(10.dp)) }

        item {
            ThirdHeader(
                title = "Смотреть все поселки",
            )
        }

        item { Spacer(Modifier.height(20.dp)) }

        item {
            Text(
                "LANDMARKET — лидирующая онлайн-платформа для продажи земельных участков*",
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                lineHeight = 24.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        item { Spacer(Modifier.height(20.dp)) }

        item { SecondMainHeader("Регионы") }

        item { Spacer(Modifier.height(10.dp)) }

        // Сетка регионов
        if (state.regions.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Регионы не найдены",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }
        } else {
            items(state.regions.chunked(2)) { pair ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    pair.forEach { region ->
                        Box(
                            modifier = Modifier.weight(1f)
                        ) {
                            CardRegion(
                                region = region
                            )
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
}


@Composable
fun MainScreenPreview() {
    MainScreen({}, {})
}