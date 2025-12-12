package com.example.landmarketmobileapp.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.landmarketmobileapp.data.Constatnt.supabase
import com.example.landmarketmobileapp.models.Profile
import com.example.landmarketmobileapp.screens.Coordinates
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.format.DateTimeFormatter


class AdvertisementViewModel : ViewModel() {

    // Состояние для списка объявлений
    private val _advertisementListState = MutableStateFlow(AdvertisementListState())
    val advertisementListState: StateFlow<AdvertisementListState> = _advertisementListState.asStateFlow()

    // Состояние для отдельного объявления
    private val _advertisementState = MutableStateFlow<AdvertisementState?>(null)
    val advertisementState: StateFlow<AdvertisementState?> = _advertisementState.asStateFlow()

    // Состояние для результатов операций
    private val _resultState = MutableStateFlow<ResultState>(ResultState.Initialized)
    val resultState: StateFlow<ResultState> = _resultState.asStateFlow()

    // Форматтер для дат
    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    private val isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    /**
     * Получение всех объявлений с фильтрацией
     */
    fun getAdvertisements(
        categoryId: String? = null,
        regionId: String? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        minArea: Double? = null,
        maxArea: Double? = null,
        hasElectricity: Boolean? = null,
        hasWater: Boolean? = null,
        hasRoad: Boolean? = null,
        searchQuery: String? = null,
        limit: Int = 20,
        offset: Int = 0
    ) {
        _advertisementListState.value = _advertisementListState.value.copy(isLoading = true)

        viewModelScope.launch {
            try {
                val query = supabase.postgrest
                    .from("advertisements")
                    .select(
                        columns = Columns.list(
                            "id",
                            "title",
                            "description",
                            "price",
                            "area",
                            "price_per_hundred",
                            "has_electricity",
                            "has_water",
                            "has_gas",
                            "has_road",
                            "has_internet",
                            "soil_type",
                            "relief_type",
                            "cadastral_number",
                            "purpose",
                            "views_count",
                            "contact_phone",
                            "contact_email",
                            "created_at",
                            "user_id",
                            "status"
                        )
                    ) {
                        // Базовые фильтры
                        filter { eq("status", "active") }

                        // Опциональные фильтры
                        categoryId?.let { filter { eq("category_id", it) } }
                        regionId?.let { filter { eq("region_id", it) } }
                        minPrice?.let { filter { gt("price", it) } }
                        maxPrice?.let { filter { lt("price", it) } }
                        minArea?.let { filter { gt("area", it) } }
                        maxArea?.let { filter { lt("area", it) } }
                        hasElectricity?.let { filter { eq("has_electricity", it) } }
                        hasWater?.let { filter { eq("has_water", it) } }
                        hasRoad?.let { filter { eq("has_road", it) } }





                    }

                val advertisements = query.decodeList<Advertisement>()

                // Преобразование в UI состояние
                val advertisementStates = advertisements.map { ad ->
                    AdvertisementState(
                        id = ad.id ?: "",
                        title = ad.title ?: "",
                        description = ad.description ?: "",
                        price = ad.price?.toInt() ?: 0,
                        area = ad.area?.toInt() ?: 0,
                        location = "", // Нужно получить из связанных таблиц
                        imageUrls = getAdvertisementImages(ad.id),
                        datePosted = "2025-12-12",
                        sellerName = "", // Нужно получить из таблицы users
                        sellerRating = 0f,
                        sellerReviewsCount = 0,
                        sellerSince = "",
                        hasElectricity = ad.hasElectricity ?: false,
                        hasWater = ad.hasWater ?: false,
                        hasRoad = ad.hasRoad ?: false,
                        hasGas = ad.hasGas ?: false,
                        hasInternet = ad.hasInternet ?: false,
                        soilType = ad.soilType ?: "Чернозем",
                        relief = "Ровный",
                        distanceToCity = 10,
                        cadastralNumber = ad.cadastralNumber ?: "",
                        purpose = ad.purpose ?: "ИЖС",
                        documents = ad.documents ?: emptyList(),
                        viewsCount = ad.viewsCount ?: 0,
                        phoneNumber =  "",
                        features = extractFeatures(ad)
                    )
                }

                _advertisementListState.value = AdvertisementListState(
                    advertisements = advertisementStates,
                    isLoading = false,
                    hasMore = advertisements.size == limit
                )

                _resultState.value = ResultState.Success("Объявления загружены")

            } catch (e: Exception) {
                Log.e("getAdvertisements", "Error loading advertisements", e)
                _advertisementListState.value = _advertisementListState.value.copy(
                    isLoading = false,
                    error = "Не удалось загрузить объявления"
                )
                _resultState.value = ResultState.Error(e.message ?: "Ошибка загрузки")
            }
        }
    }


    /**
     * Добавление/удаление из избранного
     */
    fun toggleFavorite(advertisementId: String) {
        viewModelScope.launch {
            try {
                val currentUser = supabase.auth.currentUserOrNull()
                if (currentUser == null) {}

                val isFavorite = checkIfFavorite(advertisementId)

                if (isFavorite) {
                    // Удаляем из избранного
                    supabase.postgrest
                        .from("favorites")
                        .delete {
                            filter { eq("user_id", currentUser!!.id) }
                            filter { eq("advertisement_id", advertisementId) }
                        }
                    Log.d("Favorite", "Success del")
                } else {
                    // Добавляем в избранное
                    supabase.postgrest
                        .from("favorites")
                        .insert(
                            mapOf(
                                "user_id" to currentUser!!.id,
                                "advertisement_id" to advertisementId
                            )
                        )
                    Log.d("Favorite", "Success add")
                }

                // Обновляем состояние
                _advertisementState.value?.let { currentState ->
                    _advertisementState.value = currentState.copy(isFavorite = !isFavorite)
                }

            } catch (e: Exception) {
                Log.e("toggleFavorite", "Error toggling favorite", e)
            }
        }
    }

    // Вспомогательные приватные методы

    private suspend fun getAdvertisementImages(advertisementId: String?): List<String> {
        if (advertisementId == null) return emptyList()

        return try {
            val images = supabase.postgrest
                .from("advertisement_images")
                .select {
                    filter { eq("advertisement_id", advertisementId) }
                }
                .decodeList<Map<String, Any>>()

            images.mapNotNull { it["url"] as? String }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun getSellerInfo(userId: String?): SellerInfo {
        if (userId == null) return SellerInfo()

        return try {
            val user = supabase.postgrest
                .from("users")
                .select {
                    filter { eq("id", userId) }
                }
                .decodeSingle<Map<String, Any>>()

            SellerInfo(
                name = user["full_name"] as? String ?: "",
                rating = (user["rating"] as? Double)?.toFloat() ?: 0f,
                reviewsCount = (user["reviews_count"] as? Int) ?: 0,
                since = (user["created_at"] as? String)?.let { it} ?: ""
            )
        } catch (e: Exception) {
            SellerInfo()
        }
    }

    private suspend fun getLocationInfo(villageId: String?, regionId: String?): String {
        // Здесь нужно получить названия из таблиц villages и regions
        // Возвращаем заглушку
        return "Локация"
    }

    private suspend fun checkIfFavorite(advertisementId: String): Boolean {
        val currentUser = supabase.auth.currentUserOrNull() ?: return false

        return try {
            val result = supabase.postgrest
                .from("favorites")
                .select() {
                    filter { eq("user_id", currentUser.id) }
                    filter { eq("advertisement_id", advertisementId) }
                }.decodeList<Favorites>()
                Log.d("favorite", result.size.toString())
            result.size > 0
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun incrementViewsCount(advertisementId: String) {
        try {
           /* supabase.postgrest
                .from("advertisements")
                .update(
                    mapOf(
                        "views_count" to supabase.postgrest.rpc(
                            "increment_views",
                            mapOf("ad_id" to advertisementId)
                        )
                    )
                ) {
                    filter { eq("id", advertisementId) }
                }*/
        } catch (e: Exception) {
            // Игнорируем ошибку увеличения счетчика
        }
    }

   /* private fun formatDate(dateString: String?): String {
        return try {
            dateString?.let {
                val dateTime = LocalDateTime.parse(it, isoFormatter)
                dateTime.format(dateFormatter)
            } ?: ""
        } catch (e: Exception) {
            ""
        }
    }*/

    private fun extractFeatures(advertisement: Advertisement): List<String> {
        val features = mutableListOf<String>()

        if (advertisement.hasElectricity == true) features.add("Электричество")
        if (advertisement.hasWater == true) features.add("Вода")
        if (advertisement.hasGas == true) features.add("Газ")
        if (advertisement.hasRoad == true) features.add("Дорога")
        if (advertisement.hasInternet == true) features.add("Интернет")

        return features
    }

    private fun buildAdditionalInfo(advertisement: Advertisement): String {
        val info = mutableListOf<String>()

        advertisement.soilType?.let { info.add("Тип почвы: $it") }
        advertisement.reliefType?.let { info.add("Рельеф: $it") }
        advertisement.purpose?.let { info.add("Назначение: $it") }

        return info.joinToString("\n")
    }

    private fun generateSlug(title: String): String {
        return title
            .lowercase()
            .replace("[^а-яa-z0-9\\s]".toRegex(), "")
            .replace("\\s+".toRegex(), "-")
            .trim('-')
    }

    private suspend fun uploadAdvertisementImage(advertisementId: String, imageUrl: String, orderIndex: Int) {
        try {
            supabase.postgrest
                .from("advertisement_images")
                .insert(
                    mapOf(
                        "advertisement_id" to advertisementId,
                        "url" to imageUrl,
                        "order_index" to orderIndex
                    )
                )
        } catch (e: Exception) {
            Log.e("uploadImage", "Error uploading image", e)
        }
    }

    private suspend fun getDistanceToCity(villageId: String?): Int {
        // Здесь нужно получить расстояние из таблицы villages
        return 10 // Заглушка
    }
}

// Вспомогательные data class

data class SellerInfo(
    val name: String = "",
    val rating: Float = 0f,
    val reviewsCount: Int = 0,
    val since: String = ""
)

// Model для базы данных
@kotlinx.serialization.Serializable
data class Advertisement(
    val id: String? = null,
    val userId: String? = null,
    val title: String? = null,
    val slug: String? = null,
    val description: String? = null,
    val categoryId: String? = null,
    val villageId: String? = null,
    val regionId: String? = null,
    val price: Double? = null,
    val area: Double? = null,
    val pricePerHundred: Double? = null,
    val hasElectricity: Boolean? = null,
    val hasWater: Boolean? = null,
    val hasGas: Boolean? = null,
    val hasRoad: Boolean? = null,
    val hasInternet: Boolean? = null,
    val soilType: String? = null,
    val reliefType: String? = null,
    val cadastralNumber: String? = null,
    val purpose: String? = null,
    val documents: List<String>? = null,
    val status: String? = null,
    val viewsCount: Int? = null,
    val isPremium: Boolean? = null,
    val isFeatured: Boolean? = null,
    val featuredUntil: String? = null,
    val contactPhone: String? = null,
    val contactEmail: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val publishedAt: String? = null,
    val expiresAt: String? = null
)

// UI States
data class AdvertisementListState(
    val advertisements: List<AdvertisementState> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasMore: Boolean = true
)



data class AdvertisementState(
    val id: String,
    val title: String,
    val description: String,
    val price: Int,
    val area: Int, // площадь в сотках
    val location: String?,
    val coordinates: CoordinatesState? = null,
    val imageUrls: List<String>? = emptyList(), // несколько фото
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

@Serializable
data class Favorites(
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("advertisement_id")
    val advertisementId: String,
    @SerialName("created_at")
    val createdAt: String

)
data class CoordinatesState(
    val latitude: Double,
    val longitude: Double
)

// Модель для отзывов
data class ReviewState(
    val id: String,
    val authorName: String,
    val rating: Float,
    val date: String,
    val text: String,
    val helpful: Int = 0
)

// Модель для похожих объявлений
data class SimilarAdState(
    val id: String,
    val title: String,
    val price: Int,
    val area: Int,
    val location: String,
    val imageUrl: String? = null
)

// Модель для категорий фильтров
data class CategoryState(
    val id: String,
    val name: String,
    val iconResId: Int,
    val count: Int = 0
)