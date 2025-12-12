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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.landmarketmobileapp.R
import com.example.landmarketmobileapp.components.SearchBarWithSettings
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.*

// Модель данных для объявления
// Обновите модель Advertisement для детальной информации
data class Advertisement(
    val id: String,
    val title: String,
    val description: String,
    val price: Int,
    val area: Int, // площадь в сотках
    val location: String,
    val coordinates: Coordinates? = null,
    val imageUrls: List<String> = emptyList(), // несколько фото
    val isFavorite: Boolean = false,
    val datePosted: String,
    val sellerName: String,
    val sellerRating: Float,
    val sellerReviewsCount: Int = 0,
    val sellerSince: String = "",
    val hasElectricity: Boolean = false,
    val hasWater: Boolean = false,
    val hasRoad: Boolean = false,
    val hasGas: Boolean = false,
    val hasInternet: Boolean = false,
    val soilType: String = "Чернозем",
    val relief: String = "Ровный",
    val distanceToCity: Int = 10, // км до города
    val cadastralNumber: String = "",
    val purpose: String = "ИЖС", // назначение земли
    val documents: List<String> = emptyList(),
    val viewsCount: Int = 0,
    val phoneNumber: String = "",
    val features: List<String> = emptyList(), // особенности
    val additionalInfo: String = ""
)

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

// Модель для категорий фильтров
data class Category(
    val id: String,
    val name: String,
    val iconResId: Int,
    val count: Int = 0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdsScreen(
    onNavigateDetailInform:(String)->Unit
) {
    var searchText by remember { mutableStateOf("") }
    var showFilterDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val scrollState = rememberLazyListState()

    // Фиктивные данные
    val categories = listOf(
        Category("all", "Все", R.drawable.user, 150),
        Category("cheap", "До 500К", R.drawable.ic_cheap, 45),
        Category("middle", "500К-1М", R.drawable.ic_all, 60),
        Category("premium", "От 1М", R.drawable.ic_all, 25),
        Category("electricity", "С электричеством", R.drawable.ic_electricity, 30),
        Category("water", "С водой", R.drawable.ic_water, 25),
        Category("road", "С дорогой", R.drawable.ic_all, 40),
        Category("new", "Новые", R.drawable.ic_all, 20)
    )

    val advertisements = remember {
        listOf(
            Advertisement(
                id = "1",
                title = "Участок 15 соток в коттеджном посёлке",
                description = "Ровный участок в охраняемом посёлке. Все коммуникации по границе. Отличная транспортная доступность.",
                price = 750000,
                area = 15,
                location = "Нижегородская обл., 25 км от города",
                datePosted = "2 дня назад",
                sellerName = "Александр",
                sellerRating = 4.8f,
                hasElectricity = true,
                hasWater = true,
                hasRoad = true,
                viewsCount = 124,
                phoneNumber = "+7 (999) 123-45-67"
            ),
            Advertisement(
                id = "2",
                title = "Земля под ИЖС у леса",
                description = "Участок в экологически чистом районе. Рядом лес и озеро. Идеальное место для строительства дома.",
                price = 450000,
                area = 12,
                location = "Дзержинск, 15 км",
                datePosted = "5 дней назад",
                sellerName = "ООО 'Земельные участки'",
                sellerRating = 4.5f,
                hasElectricity = true,
                hasWater = false,
                hasRoad = true,
                viewsCount = 89,
                phoneNumber = "+7 (999) 987-65-43"
            ),
            Advertisement(
                id = "3",
                title = "Участок в агрогородке 'Солнечный'",
                description = "Участок с готовым проектом дома. Все документы готовы. Возможна ипотека.",
                price = 1200000,
                area = 20,
                location = "Бор, 10 км от центра",
                datePosted = "Вчера",
                sellerName = "Застройщик 'Солнечный'",
                sellerRating = 4.9f,
                hasElectricity = true,
                hasWater = true,
                hasRoad = true,
                viewsCount = 256,
                isFavorite = true,
                phoneNumber = "+7 (999) 555-44-33"
            ),
            Advertisement(
                id = "4",
                title = "Сельхоз земля 1 га",
                description = "Плодородная земля для сельского хозяйства. Рядом река, хорошие подъездные пути.",
                price = 900000,
                area = 100,
                location = "Кстовский район",
                datePosted = "Неделю назад",
                sellerName = "Фермерское хозяйство",
                sellerRating = 4.7f,
                hasElectricity = false,
                hasWater = true,
                hasRoad = true,
                viewsCount = 67,
                phoneNumber = "+7 (999) 222-33-44"
            ),
            Advertisement(
                id = "5",
                title = "Участок в деревне с домом",
                description = "Участок с деревянным домом. Дом требует косметического ремонта. Земля плодородная.",
                price = 650000,
                area = 25,
                location = "Городец, центр деревни",
                datePosted = "3 дня назад",
                sellerName = "Мария Иванова",
                sellerRating = 4.6f,
                hasElectricity = true,
                hasWater = true,
                hasRoad = false,
                viewsCount = 143,
                phoneNumber = "+7 (999) 777-88-99"
            ),
            Advertisement(
                id = "6",
                title = "Коммерческая земля у трассы",
                description = "Земля коммерческого назначения. Идеально для АЗС, кафе, склада. Высокая проходимость.",
                price = 2500000,
                area = 50,
                location = "Трасса М7, 35 км от Н.Новгорода",
                datePosted = "Сегодня",
                sellerName = "Компания 'Трасса'",
                sellerRating = 4.8f,
                hasElectricity = true,
                hasWater = true,
                hasRoad = true,
                viewsCount = 198,
                phoneNumber = "+7 (999) 444-55-66"
            )
        )
    }

    // Фильтрация объявлений
    val filteredAds = advertisements.filter { ad ->
        val matchesSearch = searchText.isEmpty() ||
                ad.title.contains(searchText, ignoreCase = true) ||
                ad.description.contains(searchText, ignoreCase = true) ||
                ad.location.contains(searchText, ignoreCase = true)

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

    LaunchedEffect(searchText, selectedCategory) {
        isLoading = true
        delay(300) // Имитация загрузки
        isLoading = false
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

                    IconButton(
                        onClick = { /* Поделиться или другие действия */ }
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Поделиться"
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
                onSearchTextChange = { searchText = it },
                onSettingsClick = { showFilterDialog = true },
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

                if (selectedCategory != null) {
                    TextButton(
                        onClick = { selectedCategory = null }
                    ) {
                        Text("Сбросить фильтр")
                    }
                }
            }

            // Список объявлений
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF6AA26C))
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
                    state = scrollState,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(filteredAds) { ad ->
                        AdvertisementCard(
                            advertisement = ad,
                            onFavoriteClick = { /* Логика добавления в избранное */ },
                            onCardClick = {   onNavigateDetailInform(ad.id) }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))

                        // Кнопка "Показать еще"
                        if (filteredAds.size >= 6) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                OutlinedButton(
                                    onClick = { /* Загрузка дополнительных объявлений */ },
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

            if (category.count > 0) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) Color.White.copy(alpha = 0.3f) else Color.Gray.copy(alpha = 0.1f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = category.count.toString(),
                        fontSize = 10.sp,
                        color = if (isSelected) Color.White else Color.Gray,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )
                }
            }
        }
    }
}

@Composable
fun AdvertisementCard(
    advertisement: Advertisement,
    onFavoriteClick: () -> Unit,
    onCardClick: () -> Unit
) {
    val formatter = NumberFormat.getNumberInstance(Locale.getDefault())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Изображение и кнопка избранного
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color.LightGray)
            ) {
                // Здесь должно быть изображение
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF6AA26C).copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_app),
                        contentDescription = "Изображение участка",
                        tint = Color(0xFF6AA26C),
                        modifier = Modifier.size(64.dp)
                    )
                }

                // Бейдж "Новое" или "Популярное"
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
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        if (advertisement.isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "В избранное",
                        tint = if (advertisement.isFavorite) Color.Red else Color.White
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
                        text = advertisement.location,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Описание
                Text(
                    text = advertisement.description,
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
                            icon = Icons.Default.Person,
                            text = "Электричество"
                        )
                    }

                    if (advertisement.hasWater) {
                        CommunicationChip(
                            icon = Icons.Default.Person,
                            text = "Вода"
                        )
                    }

                    if (advertisement.hasRoad) {
                        CommunicationChip(
                            icon = Icons.Default.Person,
                            text = "Дорога"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Информация о продавце и дата
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

                Spacer(modifier = Modifier.height(8.dp))

                // Кнопка связи
                Button(
                    onClick = { /* Позвонить или написать */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6AA26C)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Phone,
                            contentDescription = "Позвонить",
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text("Связаться с продавцом")
                    }
                }
            }
        }
    }
}

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
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
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

