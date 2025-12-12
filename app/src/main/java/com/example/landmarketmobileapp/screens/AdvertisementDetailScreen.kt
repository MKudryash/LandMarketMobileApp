package com.example.landmarketmobileapp.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.landmarketmobileapp.R
import com.example.landmarketmobileapp.viewModels.AdvertisementState
import com.example.landmarketmobileapp.viewModels.AdvertisementViewModel
import com.example.landmarketmobileapp.viewModels.CoordinatesState
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvertisementDetailScreen(
    advertisementId: String,
    onBackClick: () -> Unit,
    viewModel: AdvertisementViewModel = viewModel()
) {
    // Загрузка данных объявления
    val advertisement by viewModel.advertisementState.collectAsState()
    val resultState by viewModel.resultState.collectAsState()

    var isFavorite by remember { mutableStateOf(false) }
    var selectedImageIndex by remember { mutableStateOf(0) }
    var showContactDialog by remember { mutableStateOf(false) }
    var showMapDialog by remember { mutableStateOf(false) }
    var showGalleryDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Загружаем объявление при открытии экрана
    LaunchedEffect(advertisementId) {
        viewModel.getAdvertisementById(advertisementId)
    }

    // Обновляем состояние избранного при загрузке объявления
    LaunchedEffect(advertisement) {
        advertisement?.let {
            isFavorite = it.isFavorite
        }
    }

    // Обработка состояний загрузки
    when (resultState) {
        is com.example.landmarketmobileapp.viewModels.ResultState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(color = Color(0xFF6AA26C))
                    Text("Загрузка объявления...")
                }
            }
            return
        }

        is com.example.landmarketmobileapp.viewModels.ResultState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
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
                        text = (resultState as com.example.landmarketmobileapp.viewModels.ResultState.Error).message,
                        fontSize = 18.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onBackClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6AA26C)
                        )
                    ) {
                        Text("Назад")
                    }
                }
            }
            return
        }

        else -> {}
    }

    // Если объявление не загружено
    if (advertisement == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
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
                    text = "Объявление не найдено",
                    fontSize = 18.sp,
                    color = Color.Gray,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onBackClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6AA26C)
                    )
                ) {
                    Text("Назад")
                }
            }
        }
        return
    }

    val ad = advertisement!!

    // Диалоги
    if (showContactDialog) {
        ContactDialog(
            phoneNumber = ad.phoneNumber,
            sellerName = ad.sellerName,
            onDismiss = { showContactDialog = false },
            onCall = {
                // Реализация звонка
                coroutineScope.launch {
                    // TODO: Реализовать звонок
                }
            },
            onMessage = {
                // Реализация отправки сообщения
                coroutineScope.launch {
                    // TODO: Реализовать отправку сообщения
                }
            }
        )
    }

    if (showMapDialog) {
        MapDialog(
            location = ad.location!!,
            coordinates = ad.coordinates,
            onDismiss = { showMapDialog = false }
        )
    }

    if (showGalleryDialog) {
        GalleryDialog(
            imageUrls = ad.imageUrls!!,
            initialIndex = selectedImageIndex,
            onDismiss = { showGalleryDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Объявление",
                        fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6AA26C),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = {
                        isFavorite = !isFavorite
                        viewModel.toggleFavorite(ad.id)
                    }) {
                        Icon(
                            if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "В избранное",
                            tint = if (isFavorite) Color.Red else Color.White
                        )
                    }

                    IconButton(onClick = { /* Поделиться */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Поделиться")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { showContactDialog = true },
                        modifier = Modifier.weight(2f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6AA26C)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Call, contentDescription = "Позвонить")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Связаться")
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
        ) {
            // Галерея изображений
            item {
                GallerySection(
                    imageUrls = ad.imageUrls!!,
                    selectedIndex = selectedImageIndex,
                    onImageSelected = { selectedImageIndex = it },
                    onGalleryClick = { showGalleryDialog = true }
                )
            }

            // Основная информация
            item {
                MainInfoSection(advertisement = ad)
            }

            // Характеристики
            item {
                CharacteristicsSection(advertisement = ad)
            }

            // Описание
            item {
                DescriptionSection(advertisement = ad)
            }

            // Особенности
            if (ad.features.isNotEmpty()) {
                item {
                    FeaturesSection(features = ad.features)
                }
            }

            // Продавец
            item {
                SellerSection(
                    sellerName = ad.sellerName,
                    sellerRating = ad.sellerRating,
                    reviewsCount = ad.sellerReviewsCount,
                    sellerSince = ad.sellerSince,
                    onContactClick = { showContactDialog = true }
                )
            }

            // Карта
            item {
                MapSection(
                    location = ad.location!!,
                    onMapClick = { showMapDialog = true }
                )
            }

            // Документы (если есть)
            if (ad.documents.isNotEmpty()) {
                item {
                    DocumentsSection(documents = ad.documents)
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun GallerySection(
    imageUrls: List<String>,
    selectedIndex: Int,
    onImageSelected: (Int) -> Unit,
    onGalleryClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Главное изображение
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(Color.LightGray)
                .clickable(onClick = onGalleryClick)
        ) {
            if (imageUrls.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrls.getOrNull(selectedIndex))
                        .crossfade(true)
                        .build(),
                    contentDescription = "Изображение участка",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
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
                        modifier = Modifier.size(80.dp)
                    )
                }
            }

            // Счетчик изображений
            if (imageUrls.size > 1) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Text(
                        text = "${selectedIndex + 1}/${imageUrls.size}",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat_medium))
                    )
                }
            }

            // Кнопка галереи
            IconButton(
                onClick = onGalleryClick,
                modifier = Modifier
                    .padding(16.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .align(Alignment.BottomEnd)
            ) {
                Icon(
                    painterResource(R.drawable.camera),
                    contentDescription = "Галерея",
                    tint = Color.White
                )
            }
        }

        // Миниатюры
        if (imageUrls.size > 1) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(imageUrls.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (index == selectedIndex) Color(0xFF6AA26C)
                                else Color.Gray.copy(alpha = 0.3f)
                            )
                            .border(
                                width = if (index == selectedIndex) 2.dp else 0.dp,
                                color = Color(0xFF6AA26C),
                                shape = RoundedCornerShape(8.dp)
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
                                modifier = Modifier.fillMaxSize(),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    color = if (index == selectedIndex) Color.White else Color.Black,
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

@Composable
fun MainInfoSection(advertisement: AdvertisementState) {
    val formatter = NumberFormat.getNumberInstance(Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
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
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${formatter.format(advertisement.price)} ₽",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6AA26C),
                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                )
                if (advertisement.area > 0) {
                    Text(
                        text = "${formatter.format(advertisement.price / advertisement.area)} ₽/сотка",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.montserrat_regular))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Основные характеристики
        Row(
            modifier = Modifier.fillMaxWidth(),
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

        Spacer(modifier = Modifier.height(12.dp))

        // Локация
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Place,
                contentDescription = "Местоположение",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = advertisement.location!!,
                fontSize = 14.sp,
                color = Color.DarkGray,
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                modifier = Modifier.weight(1f)
            )
        }

        // Кадастровый номер
        if (advertisement.cadastralNumber.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.corp_ads),
                    contentDescription = "Кадастровый номер",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Кадастр: ${advertisement.cadastralNumber}",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular))
                )
            }
        }

        // Просмотры
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(R.drawable.eye_open),
                contentDescription = "Просмотры",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "${advertisement.viewsCount} просмотров",
                fontSize = 14.sp,
                color = Color.Gray,
                fontFamily = FontFamily(Font(R.font.montserrat_regular))
            )
        }
    }
}

@Composable
fun CharacteristicItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                icon,
                contentDescription = label,
                tint = Color(0xFF6AA26C),
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )
        }

        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray,
            fontFamily = FontFamily(Font(R.font.montserrat_regular))
        )
    }
}

@Composable
fun CharacteristicsSection(advertisement: AdvertisementState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
            .padding(top = 12.dp)
    ) {
        Text(
            text = "Характеристики",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
            modifier = Modifier.padding(bottom = 12.dp)
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
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    pair.forEach { (label, value) ->
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = label,
                                fontSize = 14.sp,
                                color = Color.Gray,
                                fontFamily = FontFamily(Font(R.font.montserrat_regular))
                            )
                            Text(
                                text = value,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun DescriptionSection(advertisement: AdvertisementState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
            .padding(top = 12.dp)
    ) {
        Text(
            text = "Описание",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Text(
            text = advertisement.description,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
            color = Color.DarkGray
        )

        if (advertisement.additionalInfo.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                color = Color(0xFF6AA26C).copy(alpha = 0.1f)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = "Дополнительная информация",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6AA26C),
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = advertisement.additionalInfo,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}

@Composable
fun FeaturesSection(features: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
            .padding(top = 12.dp)
    ) {
        Text(
            text = "Особенности участка",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        androidx.compose.foundation.layout.FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            features.forEach { feature ->
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp)),
                    color = Color(0xFF6AA26C).copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, Color(0xFF6AA26C).copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Особенность",
                            tint = Color(0xFF6AA26C),
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = feature,
                            fontSize = 13.sp,
                            color = Color(0xFF6AA26C),
                            fontFamily = FontFamily(Font(R.font.montserrat_medium))
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DocumentsSection(documents: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
            .padding(top = 12.dp)
    ) {
        Text(
            text = "Документы",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Column {
            documents.forEachIndexed { index, document ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.corp_ads),
                        contentDescription = "Документ",
                        tint = Color(0xFF6AA26C),
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = document,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        modifier = Modifier.weight(1f)
                    )
                }

                if (index < documents.size - 1) {
                    Divider(modifier = Modifier.padding(start = 32.dp))
                }
            }
        }
    }
}

@Composable
fun SellerSection(
    sellerName: String,
    sellerRating: Float,
    reviewsCount: Int,
    sellerSince: String,
    onContactClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
            .padding(top = 12.dp)
    ) {
        Text(
            text = "Продавец",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Аватар продавца
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF6AA26C))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = sellerName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                )

                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Рейтинг",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = sellerRating.toString(),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )

                    Text(
                        text = " ($reviewsCount отзывов)",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.montserrat_regular))
                    )
                }

                Text(
                    text = "На сайте с $sellerSince года",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onContactClick,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6AA26C)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Call,
                        contentDescription = "Позвонить"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Позвонить")
                }
            }
        }
    }
}

@Composable
fun MapSection(
    location: String,
    onMapClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
            .padding(top = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Расположение",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )

            TextButton(onClick = onMapClick) {
                Text("Открыть карту")
            }
        }

        Text(
            text = location,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
            color = Color.DarkGray,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

        // Заглушка для карты
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF6AA26C).copy(alpha = 0.1f))
                .clickable(onClick = onMapClick),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "Карта",
                    tint = Color(0xFF6AA26C),
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Посмотреть на карте",
                    fontSize = 16.sp,
                    color = Color(0xFF6AA26C),
                    fontFamily = FontFamily(Font(R.font.montserrat_medium))
                )
            }
        }
    }
}

@Composable
fun ContactDialog(
    phoneNumber: String,
    sellerName: String,
    onDismiss: () -> Unit,
    onCall: () -> Unit,
    onMessage: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Phone,
                    contentDescription = "Телефон",
                    tint = Color(0xFF6AA26C),
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                androidx.compose.material3.Text(
                    text = "Связаться с продавцом",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                androidx.compose.material3.Text(
                    text = sellerName,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                androidx.compose.material3.Text(
                    text = phoneNumber,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6AA26C),
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            onCall()
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6AA26C)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Call, contentDescription = "Позвонить")
                            Spacer(modifier = Modifier.width(12.dp))
                            androidx.compose.material3.Text("Позвонить")
                        }
                    }

                    OutlinedButton(
                        onClick = {
                            onMessage()
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF6AA26C)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painterResource(R.drawable.message),
                                contentDescription = "Написать"
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            androidx.compose.material3.Text("Написать сообщение")
                        }
                    }

                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        androidx.compose.material3.Text("Отмена")
                    }
                }
            }
        }
    }
}

@Composable
fun MapDialog(
    location: String,
    coordinates: CoordinatesState?,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Заголовок
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    androidx.compose.material3.Text(
                        text = "Расположение участка",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Закрыть")
                    }
                }

                // Заглушка карты
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .background(Color(0xFF6AA26C).copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = "Карта",
                            tint = Color(0xFF6AA26C),
                            modifier = Modifier.size(64.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        androidx.compose.material3.Text(
                            text = location,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                       /* coordinates {
                            Spacer(modifier = Modifier.height(8.dp))

                            androidx.compose.material3.Text(
                                text = "${it.latitude}, ${it.longitude}",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                fontFamily = FontFamily(Font(R.font.montserrat_regular))
                            )
                        }*/
                    }
                }
            }
        }
    }
}

@Composable
fun GalleryDialog(
    imageUrls: List<String>,
    initialIndex: Int,
    onDismiss: () -> Unit,
) {
    var currentIndex by remember { mutableStateOf(initialIndex) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (imageUrls.isNotEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrls[currentIndex])
                            .crossfade(true)
                            .build(),
                        contentDescription = "Изображение",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = androidx.compose.ui.layout.ContentScale.Fit
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_app),
                            contentDescription = "Нет изображения",
                            tint = Color(0xFF6AA26C),
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }

                // Кнопка закрытия
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Закрыть",
                        tint = Color.White
                    )
                }

                // Навигация по изображениям
                if (imageUrls.size > 1) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                if (currentIndex > 0) currentIndex--
                            },
                            enabled = currentIndex > 0,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black.copy(alpha = 0.5f)
                            ),
                            shape = CircleShape
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        androidx.compose.material3.Text(
                            text = "${currentIndex + 1}/${imageUrls.size}",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat_medium))
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(
                            onClick = {
                                if (currentIndex < imageUrls.size - 1) currentIndex++
                            },
                            enabled = currentIndex < imageUrls.size - 1,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black.copy(alpha = 0.5f)
                            ),
                            shape = CircleShape
                        ) {
                            Icon(Icons.Default.ArrowForward, contentDescription = "Вперед")
                        }
                    }
                }
            }
        }
    }
}
