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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.landmarketmobileapp.R
import com.example.landmarketmobileapp.components.CardRegion
import com.example.landmarketmobileapp.components.CardVillage
import com.example.landmarketmobileapp.components.MainHeader
import com.example.landmarketmobileapp.components.SearchBarWithSettings
import com.example.landmarketmobileapp.components.SecondHeader
import com.example.landmarketmobileapp.components.SecondMainHeader
import com.example.landmarketmobileapp.components.ThirdHeader
import com.example.landmarketmobileapp.viewModels.MainViewModel
import okhttp3.internal.http2.Header
import java.text.NumberFormat
import java.util.Locale
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
fun ItemAboutUs(
    number: Int,
    text: String,
) {
    val formatter = NumberFormat.getNumberInstance(Locale.getDefault())
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.more),
            tint = Color(0xFF6AA26C),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(8.dp))
        Column {
            Text(
                formatter.format(number),
                color = Color(0xFF6AA26C),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )
            Text(
                text,
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_regular))
            )
        }
    }
}

@Composable
fun ErrorScreen(
    error: String,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD9D9D9))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = error,
                color = Color.Red,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6AA26C)
                    )
                ) {
                    Text("Повторить")
                }

                TextButton(onClick = onDismiss) {
                    Text("Закрыть")
                }
            }
        }
    }
}



@Composable
fun MainScreenPreview() {
    MainScreen({}, {})
}