package com.example.landmarketmobileapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.landmarketmobileapp.R
import com.example.landmarketmobileapp.components.AdvertisementCard
import com.example.landmarketmobileapp.components.CategoryChip
import com.example.landmarketmobileapp.components.SearchBarWithSettings
import com.example.landmarketmobileapp.viewModels.AdvertisementViewModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Модель данных для объявления
// Обновите модель Advertisement для детальной информации

data class Coordinates(
    val latitude: Double,
    val longitude: Double
)

@Serializable
@SerialName("seller_reviews")
data class Review(
    val id: String,
    val reviewer_id: String,
    val seller_id: String,
    val advertisement_id: String,
    val rating: Float,
    val title: String,
    val comment: String,
    val helpful_count: Int = 0,
    val is_verified_purchase: Boolean,
    val is_approved: Boolean,
    val created_at: String,
    val updated_at: String?,
)

// Модель для похожих объявлений
data class SimilarAd(
    val id: String,
    val title: String,
    val price: Int,
    val area: Int,
    val location: String,
    val imageUrl: String? = null
)



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdsScreen(
    onNavigateDetailInform: (String) -> Unit,
    viewModel: AdvertisementViewModel = viewModel()
) {
    val uiState by viewModel.advertisementListState.collectAsState()
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    // Инициализация загрузки при первом открытии
    LaunchedEffect(Unit) {
        viewModel.getAdvertisements()
    }

    // Фильтрация объявлений
    val filteredAds = uiState.advertisements.filter { ad ->
        val matchesSearch = searchText.isEmpty() ||
                ad.title.contains(searchText, ignoreCase = true) ||
                ad.description.contains(searchText, ignoreCase = true) ||
                ad.location!!.contains(searchText, ignoreCase = true)

        val matchesCategory = when (selectedCategory) {
            "cheap" -> ad.price <= 500000
            "middle" -> ad.price in 500001..1000000
            "premium" -> ad.price > 1000000
            "electricity" -> ad.hasElectricity
            "water" -> ad.hasWater
            "road" -> ad.hasRoad
            "new" -> ad.datePosted.contains("час") || ad.datePosted.contains("сегодня") || ad.datePosted.contains("вчера")
            else -> true
        }

        matchesSearch && matchesCategory
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Объявления",
                        fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6AA26C),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    IconButton(
                        onClick = { /* Действия с избранным */ }
                    ) {
                        Icon(
                            Icons.Default.FavoriteBorder,
                            contentDescription = "Избранное"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
        ) {
            // Поисковая строка
            SearchBarWithSettings(
                searchText = searchText,
                onSearchTextChange = {
                    searchText = it
                    // Можно добавить поиск с задержкой
                    if (it.length > 2) {
                        viewModel.getAdvertisements(searchQuery = it)
                    }
                },
                onSettingsClick = { /* Показать фильтры */ },
                placeholder = "Поиск объявлений...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )

            // Категории фильтров
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(end = 16.dp)
            ) {
                items(categories) { category ->
                    CategoryChip(
                        category = category,
                        isSelected = selectedCategory == category.id,
                        onClick = {
                            selectedCategory =
                                if (selectedCategory == category.id) null else category.id
                        }
                    )
                }
            }

            // Счетчик найденных объявлений
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Найдено ${filteredAds.size} объявлений",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = FontFamily(Font(R.font.montserrat_medium))
                )

                if (selectedCategory != null || searchText.isNotEmpty()) {
                    TextButton(
                        onClick = {
                            selectedCategory = null
                            searchText = ""
                            viewModel.getAdvertisements()
                        }
                    ) {
                        Text("Сбросить фильтр")
                    }
                }
            }

            // Список объявлений
            if (uiState.isLoading && filteredAds.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator(color = Color(0xFF6AA26C))
                        Text("Загрузка объявлений...")
                    }
                }
            } else if (uiState.error != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = "Ошибка",
                            tint = Color.Red,
                            modifier = Modifier.size(64.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = uiState.error ?: "Произошла ошибка",
                            fontSize = 18.sp,
                            color = Color.Gray,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.getAdvertisements() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF6AA26C)
                            )
                        ) {
                            Text("Повторить попытку")
                        }
                    }
                }
            } else if (filteredAds.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Не найдено",
                            tint = Color.Gray,
                            modifier = Modifier.size(64.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Объявления не найдены",
                            fontSize = 18.sp,
                            color = Color.Gray,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        )

                        Text(
                            text = "Попробуйте изменить параметры поиска",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            fontFamily = FontFamily(Font(R.font.montserrat_regular))
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                searchText = ""
                                selectedCategory = null
                                viewModel.getAdvertisements()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF6AA26C)
                            )
                        ) {
                            Text("Сбросить фильтры")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(filteredAds) { ad ->
                        AdvertisementCard(
                            advertisement = ad,
                            viewModel = viewModel,
                            onCardClick = { onNavigateDetailInform(ad.id) }
                        )
                    }

                    // Индикатор загрузки внизу при подгрузке
                    if (uiState.isLoading && filteredAds.isNotEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = Color(0xFF6AA26C),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }

                    // Кнопка "Показать еще" если есть еще объявления
                    if (uiState.hasMore && !uiState.isLoading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        viewModel.getAdvertisements(
                                            limit = 20,
                                            offset = filteredAds.size
                                        )
                                    },
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color(0xFF6AA26C)
                                    ),
                                    border = ButtonDefaults.outlinedButtonBorder.copy(
                                        width = 1.dp
                                    )
                                ) {
                                    Text("Показать ещё")
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        Icons.Default.Refresh,
                                        contentDescription = "Обновить",
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
