package com.example.landmarketmobileapp.presentation.viewModels


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.landmarketmobileapp.data.Constants.supabase
import com.example.landmarketmobileapp.models.state.MainScreenState
import com.example.landmarketmobileapp.models.Region
import com.example.landmarketmobileapp.models.state.RegionUI
import com.example.landmarketmobileapp.models.Village
import com.example.landmarketmobileapp.models.state.VillageUI
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        _state.value = _state.value.copy(isLoading = true)

        viewModelScope.launch {
            try {
                // Загружаем данные последовательно, чтобы обрабатывать ошибки
                loadVillages()
                loadRegions()
                loadStats()

                _state.value = _state.value.copy(isLoading = false)

            } catch (e: Exception) {
                Log.e("MainViewModel", "Error loading data", e)
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Ошибка загрузки данных: ${e.message}"
                )
            }
        }
    }

    private suspend fun loadVillages() {
        try {
            Log.d("MainViewModel", "Loading villages...")

            // Сначала получим raw данные для отладки
            val rawResponse = supabase.postgrest
                .from("villages")
                .select()

            Log.d("MainViewModel", "Raw villages response: $rawResponse")

            // Теперь получим данные с явным указанием полей
            val villages = supabase.postgrest
                .from("villages")
                .select(
                    columns = Columns.list(
                        "id",
                        "name",
                        "slug",
                        "description",
                        "distance_to_city",
                        "image_url",
                        "cost_of_the_plot",
                        "cost_per_hundred"
                    )
                ) {
                    filter { eq("is_active", true) }
                    limit(8)
                }
                .decodeList<Village>()

            Log.d("MainViewModel", "Loaded ${villages.size} villages")

            val villageUIs = villages.map { village ->
                val plotPrice = village.costOfThePlot ?: getMockPrice(village.name)
                val perHundredPrice = village.costPerHundred ?: getMockMinPrice(village.name)
                val distance = village.distanceToCity?.toInt() ?: 0

                VillageUI(
                    id = village.id,
                    name = village.name,
                    price = plotPrice,
                    minPrice = perHundredPrice,
                    location = if (distance > 0) "$distance км от города" else "Пригород",
                    description = village.description,
                    distanceToCity = village.distanceToCity,
                    slug = village.slug,
                    imageUrl = village.image ?: "",
                    costPerHundred = village.costPerHundred ?: 10000,
                    costOfThePlot = village.costOfThePlot ?: 10000
                )
            }

            _state.value = _state.value.copy(villages = villageUIs)

        } catch (e: Exception) {
            Log.e("MainViewModel", "Error loading villages", e)
            throw e
        }
    }

    private suspend fun loadRegions() {
        try {
            Log.d("MainViewModel", "Loading regions...")

            // Используем явное указание полей
            val regions = supabase.postgrest
                .from("regions")
                .select(
                    columns = Columns.list(
                        "id",
                        "name",
                        "address",
                        "description",
                        "image_url"
                    )
                ) {
                    filter { eq("is_active", true) }
                    limit(6)
                }
                .decodeList<Region>()

            Log.d("MainViewModel", "Loaded ${regions.size} regions")

            val regionUIs = regions.map { region ->
                RegionUI(
                    id = region.id,
                    name = region.name,
                    address = region.address ?: "",
                    price = 150000, // Временное значение
                    location = "Регион",
                    description = region.description,
                    slug = "region",
                    imageUrl = region.image ?: "",
                    costPerHundred = 10000
                )
            }

            _state.value = _state.value.copy(regions = regionUIs)

        } catch (e: Exception) {
            Log.e("MainViewModel", "Error loading regions", e)
            // Не выбрасываем исключение, чтобы не ломать весь экран
            Log.w("MainViewModel", "Continuing without regions data")
        }
    }

    private fun loadStats() {
        try {
            // Mock статистика пока
            val stats = mapOf(
                "предложений" to 10000,
                "продавцов" to 7600,
                "сделок" to 25000
            )

            _state.value = _state.value.copy(stats = stats)

        } catch (e: Exception) {
            Log.e("MainViewModel", "Error loading stats", e)
            // Используем mock данные если не удалось загрузить
            _state.value = _state.value.copy(stats = getMockStats())
        }
    }

    // Временные функции для mock данных
    private fun getMockPrice(villageName: String): Int {
        return when {
            villageName.contains("Бахтаево", ignoreCase = true) -> 600000
            villageName.contains("Сосновый", ignoreCase = true) -> 450000
            villageName.contains("Речной", ignoreCase = true) -> 800000
            villageName.contains("Зелен", ignoreCase = true) -> 550000
            else -> 500000
        }
    }

    private fun getMockMinPrice(villageName: String): Int {
        return when {
            villageName.contains("Бахтаево", ignoreCase = true) -> 150000
            villageName.contains("Сосновый", ignoreCase = true) -> 120000
            villageName.contains("Речной", ignoreCase = true) -> 200000
            villageName.contains("Зелен", ignoreCase = true) -> 180000
            else -> 100000
        }
    }

    private fun getMockStats(): Map<String, Int> {
        return mapOf(
            "предложений" to 10000,
            "продавцов" to 7600,
            "сделок" to 25000
        )
    }

    fun updateSearchText(text: String) {
        _searchText.value = text
        filterData(text)
    }

    private fun filterData(searchText: String) {
        viewModelScope.launch {
            if (searchText.isBlank()) {
                loadData()
            } else {
                _state.value = _state.value.copy(isLoading = true)

                try {
                    // Фильтрация деревень
                    val filteredVillages = supabase.postgrest
                        .from("villages")
                        .select(
                            columns = Columns.list(
                                "id",
                                "name",
                                "slug",
                                "description",
                                "distance_to_city",
                                "image_url",
                                "cost_of_the_plot",
                                "cost_per_hundred"
                            )
                        ) {
                            filter {
                                eq("is_active", true)
                                or {
                                    ilike("name", "%$searchText%")
                                    ilike("description", "%$searchText%")
                                }
                            }
                            limit(8)
                        }
                        .decodeList<Village>()

                    val villageUIs = filteredVillages.map { village ->
                        val plotPrice = village.costOfThePlot ?: getMockPrice(village.name)
                        val perHundredPrice = village.costPerHundred ?: getMockMinPrice(village.name)
                        val distance = village.distanceToCity?.toInt() ?: 0

                        VillageUI(
                            id = village.id,
                            name = village.name,
                            price = plotPrice,
                            minPrice = perHundredPrice,
                            location = if (distance > 0) "$distance км от города" else "Пригород",
                            description = village.description,
                            distanceToCity = village.distanceToCity,
                            slug = village.slug,
                            imageUrl = village.image ?: "",
                            costPerHundred = village.costPerHundred ?: 10000,
                            costOfThePlot = village.costOfThePlot ?: 10000
                        )
                    }

                    _state.value = _state.value.copy(
                        villages = villageUIs,
                        isLoading = false,
                        error = null
                    )

                } catch (e: Exception) {
                    Log.e("MainViewModel", "Error filtering data", e)
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "Ошибка поиска: ${e.message}"
                    )
                }
            }
        }
    }

    fun refreshData() {
        loadData()
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}


