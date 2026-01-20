package com.example.landmarketmobileapp.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.landmarketmobileapp.R
import com.example.landmarketmobileapp.components.CharacteristicsSection
import com.example.landmarketmobileapp.components.ContactDialog
import com.example.landmarketmobileapp.components.DescriptionSection
import com.example.landmarketmobileapp.components.DocumentsSection
import com.example.landmarketmobileapp.components.FeaturesSection
import com.example.landmarketmobileapp.components.GalleryDialog
import com.example.landmarketmobileapp.components.GallerySection
import com.example.landmarketmobileapp.components.MainInfoSection
import com.example.landmarketmobileapp.components.MapDialog
import com.example.landmarketmobileapp.components.MapSection
import com.example.landmarketmobileapp.components.SellerSection
import com.example.landmarketmobileapp.viewModels.AdvertisementViewModel
import kotlinx.coroutines.launch

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
            onDismiss = { showMapDialog = false },
            center_longitude = ad.center_longitude ?: 0.0,
            center_latitude = ad.center_latitude ?: 0.0
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
                    sellerImage = ad.sellerImage!!,
                    sellerName = ad.sellerName,
                    sellerRating = ad.sellerRating,
                    reviewsCount = ad.sellerReviewsCount,
                    sellerSince = ad.sellerSince,
                    onContactClick = { showContactDialog = true },
                    review = ad.reviews!!
                )
            }

            // Карта
            item {
                MapSection(
                    center_latitude = ad.center_latitude ?: 0.0,
                    center_longitude = ad.center_longitude ?: 0.0,
                    onMapClick = { showMapDialog = true },
                    location = ad.location!!
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

