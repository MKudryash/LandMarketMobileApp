package com.example.landmarketmobileapp.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.landmarketmobileapp.data.Constants.supabase
import com.example.landmarketmobileapp.models.Profile
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {
    private val _uiState = mutableStateOf(ProfileState())
    val uiState: ProfileState get() = _uiState.value

    private val _resultState = MutableStateFlow<ResultState>(ResultState.Initialized)
    val resultState: StateFlow<ResultState> = _resultState.asStateFlow()

    private val _resultStateUpdate = MutableStateFlow<ResultState>(ResultState.Loading)
    val resultStateUpdate: StateFlow<ResultState> = _resultStateUpdate.asStateFlow()

    private val _passwordChangeState = MutableStateFlow<ResultState>(ResultState.Loading)
    val passwordChangeState: StateFlow<ResultState> = _passwordChangeState.asStateFlow()

    lateinit var user: Profile

    fun updateState(newState: ProfileState) {
        _uiState.value = newState
    }
    init {
        getUser()
    }

    fun getUser() {
        _resultState.value = ResultState.Loading
        viewModelScope.launch {
            try {
                // 1. Check if the user is authenticated
                val currentUser = supabase.auth.currentUserOrNull()
                if (currentUser == null) {
                    _resultState.value = ResultState.Error("User not authenticated")
                    return@launch
                }

                // 2. Try to fetch the user's profile
                val result = supabase.postgrest
                    .from("users")
                    .select() {
                        filter {
                            eq("id", currentUser.id)
                        }
                    }
                    .decodeList<Profile>() // Decode as a list, not a single item

                // 3. Check if the list has data
                if (result.isEmpty()) {
                    // Profile doesn't exist. You may need to create one.
                    Log.w("getUser", "Profile not found for user ID: ${currentUser.id}")
                    _resultState.value = ResultState.Error("User profile not found")

                    // Optional: You could call a function here to create a default profile
                    // createUserProfile(currentUser.id)
                } else {
                    // Success: Get the first (and should be only) profile
                    user = result.first()

                    _uiState.value = ProfileState(
                        user.email,
                        user.phone,
                        username = user.fullName,
                        imageUrl = user.image
                    )
                    _resultState.value = ResultState.Success("Success")
                }

            } catch (e: Exception) { // Catch all exceptions, not just AuthRestException
                Log.e("getUser", "Error loading user data", e)
                _resultState.value = ResultState.Error(
                    when (e) {
                        is AuthRestException -> "Authentication error: ${e.message}"
                        else -> "Failed to load profile: ${e.message}"
                    }
                )
            }
        }
    }
    fun updateUser() {
        _resultStateUpdate.value = ResultState.Loading
        viewModelScope.launch {
            try {
                // 1. Получаем текущего пользователя
                val currentUser = supabase.auth.currentUserOrNull()
                if (currentUser == null) {
                    _resultStateUpdate.value = ResultState.Error("Пользователь не авторизован")
                    return@launch
                }

                // 2. Обновляем данные в таблице USERS (не books)
                supabase.postgrest.from("users").update(
                    {

                        set("email", _uiState.value.email)
                        set("phone", _uiState.value.telephone) // телефон
                        set("full_name", _uiState.value.username) // полное имя
                    }
                ) {
                    filter {
                        eq("id", currentUser.id) // обновляем только текущего пользователя
                    }
                }

                // 3. Обновляем локальные данные
                _uiState.value = ProfileState(
                    _uiState.value.email,
                    _uiState.value.telephone,
                    username = _uiState.value.username,
                    imageUrl = _uiState.value.imageUrl
                )

                _resultStateUpdate.value = ResultState.Success("Данные успешно обновлены")

            } catch (e: Exception) {
                Log.e("updateUser", "Ошибка обновления данных", e)
                _resultStateUpdate.value = ResultState.Error(
                    when (e) {
                        is AuthRestException -> "Ошибка авторизации: ${e.message}"
                        else -> "Не удалось обновить данные: ${e.message}"
                    }
                )
            }
        }
    }
    fun requestPasswordReset() {
        // Валидация email перед отправкой
        if (_uiState.value.email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(_uiState.value.email).matches()) {
            _passwordChangeState.value = ResultState.Error("Введите корректный email адрес")
            return
        }

        _passwordChangeState.value = ResultState.Loading
        viewModelScope.launch {
            try {
                // Отправляем ссылку для сброса пароля на email
                supabase.auth.resetPasswordForEmail(
                    email = _uiState.value.email
                )

                _passwordChangeState.value = ResultState.Success("Ссылка для сброса пароля отправлена на ${_uiState.value.email}")

            } catch (e: Exception) {
                Log.e("requestPasswordReset", "Error requesting password reset", e)
                _passwordChangeState.value = ResultState.Error(
                    when {
                        e.message?.contains("rate limit") == true ->
                            "Слишком много запросов. Попробуйте позже"
                        e.message?.contains("not found") == true ->
                            "Пользователь с таким email не найден"
                        else -> "Ошибка: ${e.message ?: "Неизвестная ошибка"}"
                    }
                )
            }
        }
    }
}

data class ProfileState(
    val email: String = "",
    val telephone: String = "",
    val imageUrl: String? = "",
    val password: String? = "",
    val confirmPassword: String? = "",
    val username: String = "",
    var isEmailError:Boolean = false
)