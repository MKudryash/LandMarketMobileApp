package com.example.landmarketmobileapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.landmarketmobileapp.R
import com.example.landmarketmobileapp.components.SearchBarWithSettings
import com.example.landmarketmobileapp.viewModels.AdvertisementState
import com.example.landmarketmobileapp.viewModels.AdvertisementViewModel
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.*

// Модель данных для объявления
// Обновите модель Advertisement для детальной информации

data class Coordinates(
    val latitude: Double,
    val longitude: Double
)

// Модель для отзывов
data class Review(
    val id: String,
    val authorName: String,
    val rating: Float,
    val date: String,
    val text: String,
    val helpful: Int = 0
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
                            selectedCategory = if (selectedCategory == category.id) null else category.id
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

@Composable
fun AdvertisementCard(
    advertisement: AdvertisementState,
    viewModel: AdvertisementViewModel,
    onCardClick: () -> Unit
) {
    val formatter = NumberFormat.getNumberInstance(Locale.getDefault())
    var isFavorite by remember { mutableStateOf(advertisement.isFavorite) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Изображение
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color.LightGray)
            ) {
                if (advertisement.imageUrls!!.isNotEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(advertisement.imageUrls.first())
                            .crossfade(true)
                            .build(),
                        contentDescription = "Изображение участка",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF6AA26C).copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_app),
                            contentDescription = "Нет изображения",
                            tint = Color(0xFF6AA26C),
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }

                // Бейдж "Новое"
                if (advertisement.datePosted.contains("сегодня") || advertisement.datePosted.contains("вчера")) {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Red)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "НОВОЕ",
                            fontSize = 10.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
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
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        if (isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "В избранное",
                        tint = if (isFavorite) Color.Red else Color.White
                    )
                }
            }

            // Контент карточки
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Заголовок и цена
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = advertisement.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "${formatter.format(advertisement.price)} ₽",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6AA26C),
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Площадь и локация
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_area),
                        contentDescription = "Площадь",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "${advertisement.area} сот.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.montserrat_medium))
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Местоположение",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = advertisement.location!!,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Короткое описание
                Text(
                    text = advertisement.description.take(100) + if (advertisement.description.length > 100) "..." else "",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Коммуникации
                Row(
                    modifier = Modifier.fillMaxWidth(),
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

                Spacer(modifier = Modifier.height(12.dp))

                // Продавец и дата
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Аватар продавца
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF6AA26C))
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Продавец",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Text(
                                text = advertisement.sellerName,
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.montserrat_medium))
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = "Рейтинг",
                                    tint = Color(0xFFFFC107),
                                    modifier = Modifier.size(12.dp)
                                )

                                Spacer(modifier = Modifier.width(2.dp))

                                Text(
                                    text = advertisement.sellerRating.toString(),
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    fontFamily = FontFamily(Font(R.font.montserrat_medium))
                                )
                            }
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = advertisement.datePosted,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontFamily = FontFamily(Font(R.font.montserrat_regular))
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painterResource(R.drawable.eye_open),
                                contentDescription = "Просмотры",
                                tint = Color.Gray,
                                modifier = Modifier.size(12.dp)
                            )

                            Spacer(modifier = Modifier.width(2.dp))

                            Text(
                                text = advertisement.viewsCount.toString(),
                                fontSize = 12.sp,
                                color = Color.Gray,
                                fontFamily = FontFamily(Font(R.font.montserrat_regular))
                            )
                        }
                    }
                }
            }
        }
    }
}

// Категории фильтров (можно вынести в отдельный файл)
val categories = listOf(
    Category("all", "Все", R.drawable.ic_all, 0),
    Category("cheap", "До 500к", R.drawable.ic_cheap, 0),
    Category("middle", "500к-1м", R.drawable.ic_cheap, 0),
    Category("premium", "Премиум", R.drawable.ic_cheap, 0),
    Category("electricity", "Свет", R.drawable.ic_electricity, 0),
    Category("water", "Вода", R.drawable.ic_water, 0),
    Category("road", "Дорога", R.drawable.ic_cheap, 0),
    Category("new", "Новые", R.drawable.ic_cheap, 0)
)

data class Category(
    val id: String,
    val name: String,
    val iconResId: Int,
    val count: Int = 0
)

@Composable
fun CommunicationChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp)),
        color = Color(0xFF6AA26C).copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = text,
                tint = Color(0xFF6AA26C),
                modifier = Modifier.size(12.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = text,
                fontSize = 10.sp,
                color = Color(0xFF6AA26C),
                fontFamily = FontFamily(Font(R.font.montserrat_medium))
            )
        }
    }
}

@Composable
fun CategoryChip(
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        color = if (isSelected) Color(0xFF6AA26C) else Color.White,
        tonalElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                painter = painterResource(id = category.iconResId),
                contentDescription = category.name,
                tint = if (isSelected) Color.White else Color(0xFF6AA26C),
                modifier = Modifier.size(16.dp)
            )

            Text(
                text = category.name,
                fontSize = 12.sp,
                color = if (isSelected) Color.White else Color.Black,
                fontFamily = FontFamily(Font(R.font.montserrat_medium))
            )
        }
    }
}

// Для icon drawable ресурсов создайте следующие файлы в res/drawable:
/*
ic_all.xml, ic_cheap.xml, ic_middle.xml, ic_premium.xml,
ic_electricity.xml, ic_water.xml, ic_road.xml, ic_new.xml,
ic_area.xml
*/

